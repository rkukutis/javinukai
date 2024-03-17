package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.ParticipationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.mapper.ContestMapper;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.ParticipationRequestRepository;
import lt.javinukai.javinukai.wrapper.ContestWrapper;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContestService {

    private final CategoryRepository categoryRepository;
    private final ContestRepository contestRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CompetitionRecordRepository recordRepository;
    private final CompetitionRecordService competitionRecordService;
    private final PhotoService photoService;


    @Transactional
    public Contest createContest(ContestDTO contestDTO, MultipartFile thumbnailFile) {
        final Contest contest = ContestMapper.contestDTOToContest(contestDTO);
        final Contest createdContest = contestRepository.save(contest);

        final List<Category> categoryList = new ArrayList<>();
        for (Category category : contestDTO.getCategories()) {
            final Category categoryIn = categoryRepository
                    .findByNameAndDescriptionAndMaxTotalSubmissions(
                            category.getName(),
                            category.getDescription(),
                            category.getMaxTotalSubmissions());

                if (categoryIn == null) {
                    throw new EntityNotFoundException("category was not found with ID: " + category.getId());
                }
            categoryList.add(categoryIn);
        }

        createdContest.setCategories(categoryList);
        createdContest.setThumbnailURL(photoService.generatedContestThumbnail(createdContest.getId(), thumbnailFile));
        contestRepository.save(createdContest);

        log.info("{}: Created and added new contest to database", this.getClass().getName());
        return createdContest;
    }

    public Page<Contest> retrieveAllContests(Pageable pageable, String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            log.info("{}: Retrieving all contests list from database", this.getClass().getName());
            return contestRepository.findAll(pageable);
        } else {
            log.info("{}: Retrieving categories by name", this.getClass().getName());
            return contestRepository.findByNameContainingIgnoreCase(pageable, keyword);
        }
    }

    public Contest retrieveContest(UUID id) {
        final Contest contestToShow = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        log.info("Retrieving contest from database, name - {}", id);
        return contestToShow;
    }

    public ContestWrapper retrieveContest(UUID contestId, User requestingUser) {
        final Contest contestToShow = contestRepository.findById(contestId).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + contestId));
        log.info("Retrieving contest from database, name - {}", contestId);

        long totalContestEntries = recordRepository.findByContestId(contestId).stream()
                .map(record -> record.getEntries().size())
                .reduce(Integer::sum).orElse(0);

        ContestWrapper.ContestWrapperBuilder wrapperBuilder = ContestWrapper.builder()
                .contest(contestToShow)
                .totalEntries(totalContestEntries);

        if (requestingUser != null) {
            List<ParticipationRequest> userRequests = participationRequestRepository
                    .findByUserIdAndContestId(requestingUser.getId(), contestId);
            System.out.println(userRequests.size());

            long totalUserEntries = recordRepository.findByContestIdAndUserId(contestId, requestingUser.getId()).stream()
                    .map(record -> record.getEntries().size())
                    .reduce(Integer::sum).orElse(0);
            System.out.println(totalUserEntries);

            wrapperBuilder
                    .maxUserEntries(requestingUser.getMaxTotal())
                    .userEntries(totalUserEntries)
                    .status(userRequests.isEmpty() ? null : userRequests.get(0).getRequestStatus());
        }
        return wrapperBuilder.build();
    }

    @Transactional
    public Contest updateContest(UUID id, ContestDTO contestDTO, MultipartFile file) {
        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        contestToUpdate.setName(contestDTO.getName());

        if (file != null && file.getContentType().startsWith("image")) {
            String thumbnailURL = photoService.generatedContestThumbnail(id, file);
            contestToUpdate.setThumbnailURL(thumbnailURL);
        }

        contestToUpdate.setDescription(contestDTO.getDescription());
        contestToUpdate.setMaxTotalSubmissions(contestDTO.getMaxTotalSubmissions());
        contestToUpdate.setMaxUserSubmissions(contestDTO.getMaxUserSubmissions());
        contestToUpdate.setStartDate(contestDTO.getStartDate());
        contestToUpdate.setEndDate(contestDTO.getEndDate());

        log.info("{}: Updating contest", this.getClass().getName());
        return contestRepository.save(contestToUpdate);
    }

    @Transactional
    // if some categories are deleted, corresponding records must also be deleted
    // if categories are added, new records must be created for approved users
    public Contest updateCategoriesOfContest(UUID id, List<Category> categories) {

        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));

        List<Category> newCategories = new ArrayList<>();

        for (Category category : categories) {
            final Category categoryIn = categoryRepository.findById(category.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Category was not found with ID: " + category.getId()));
            newCategories.add(categoryIn);
        }
        List<Category> extraCategories = new ArrayList<>();
        List<Category> removedCategories = new ArrayList<>();
        List<Category> oldCategories = contestToUpdate.getCategories();
        for (Category oldCategory : oldCategories) {
            if (!newCategories.contains(oldCategory)) {
                removedCategories.add(oldCategory);
            }
        }
        for (Category newCategory : newCategories) {
            if (!oldCategories.contains(newCategory)) {
                extraCategories.add(newCategory);
            }
        }

        if (!extraCategories.isEmpty()) {
           competitionRecordService.createRecordsForApprovedUsers(extraCategories, contestToUpdate.getId());
        }
        if (!removedCategories.isEmpty()) {
            competitionRecordService.deleteRecordsForCategories(removedCategories, contestToUpdate.getId());
        }


        contestToUpdate.setCategories(newCategories);
        return contestRepository.save(contestToUpdate);
    }

    @Transactional
    public void deleteContest(UUID id) {
        if (contestRepository.existsById(id)) {
            contestRepository.deleteById(id);
            log.info("{}: Deleted contest from the database with ID: {}", this.getClass().getName(), id);
        } else {
            throw new EntityNotFoundException("Contest was not found with ID: " + id);
        }
    }

    public byte[] retrieveContestThumbnail(UUID id) {
        return photoService.getThumbnailBytes(id);
    }

    public List<String> retrieveLatestContestURLs(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        List<Contest> latestContests = contestRepository.findByOrderByCreatedAtDesc(Limit.of(limit));
        return latestContests.stream().map(Contest::getThumbnailURL).filter(Objects::nonNull).toList();
    }
}