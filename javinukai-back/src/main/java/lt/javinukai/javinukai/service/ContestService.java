package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.*;
import lt.javinukai.javinukai.mapper.ContestMapper;
import lt.javinukai.javinukai.repository.*;
import lt.javinukai.javinukai.wrapper.ContestWrapper;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContestService {

    private final CategoryRepository categoryRepository;
    private final ContestRepository contestRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CompetitionRecordRepository recordRepository;
    private final CompetitionRecordService competitionRecordService;
    private final PhotoCollectionRepository collectionRepository;
    private final PhotoService photoService;
    private final RateService rateService;
    private final ParticipationRequestService requestService;

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

        Page<Contest> page;
        if (keyword == null || keyword.isEmpty()) {
            log.info("{}: Retrieving all contests list from database", this.getClass().getName());
            page = contestRepository.findByIsArchived(pageable, false);
        } else {
            log.info("{}: Retrieving categories by name", this.getClass().getName());
            page = contestRepository.findByNameContainingIgnoreCaseAndIsArchived(pageable, keyword, false);
        }

        List<Contest> filteredContests = page.getContent().stream()
                .filter((contest) -> !contest.isArchived())
                .collect(Collectors.toList());

        return new PageImpl<>(filteredContests, pageable, page.getTotalElements());
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

        int totalContestEntries = collectionRepository
                .findCollectionsByContestId(contestToShow.getId()).size();

        ContestWrapper.ContestWrapperBuilder wrapperBuilder = ContestWrapper.builder()
                .contest(contestToShow)
                .totalEntries(totalContestEntries);

        if (requestingUser != null) {
            List<ParticipationRequest> userRequests = participationRequestRepository
                    .findByUserIdAndContestId(requestingUser.getId(), contestId);
            System.out.println(userRequests.size());

            int totalUserEntries = collectionRepository
                    .findCollectionsByContestIdAndUserId(contestToShow.getId(), requestingUser.getId()).size();

            wrapperBuilder
                    .maxUserEntries(contestToShow.getMaxUserSubmissions() <= requestingUser.getMaxTotal()
                            && !requestingUser.isCustomLimits() ?
                            contestToShow.getMaxUserSubmissions() :
                            requestingUser.getMaxTotal()
                    )
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
            Contest c = contestRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("not found"));
            requestService.deleteAllRequestsByContestId(id);
            competitionRecordService.deleteRecordsForCategories(c.getCategories(), id);
            contestRepository.deleteById(id);
            log.info("{}: Deleted contest from the database with ID: {}", this.getClass().getName(), id);
        } else {
            throw new EntityNotFoundException("Contest was not found with ID: " + id);
        }
    }

    @Transactional
    public void startNewCompetitionStage(UUID contestId) {
        List<PhotoCollection> collections = rateService.findAllCollectionsInContest(contestId);
        collections
                .forEach(c -> {
                    if (c.getLikesCount() == 0) {
                        c.setHidden(true);
                    } else {
                        c.removeAllLikesFromCollection();
                        c.setLikesCount(0);
                    }
                });
        rateService.updateCollections(collections);
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