package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.*;
import lt.javinukai.javinukai.mapper.ContestMapper;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.ParticipationRequestRepository;
import lt.javinukai.javinukai.wrapper.ContestWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContestService {

    private final CategoryRepository categoryRepository;
    private final ContestRepository contestRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CompetitionRecordRepository recordRepository;
    private final RateService rateService;


    @Transactional
    public Contest createContest(ContestDTO contestDTO) {
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
        contestRepository.save(contest);

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
    public Contest updateContest(UUID id, ContestDTO contestDTO) {

        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        contestToUpdate.setName(contestDTO.getName());
        contestToUpdate.setDescription(contestDTO.getDescription());
        contestToUpdate.setMaxTotalSubmissions(contestDTO.getMaxTotalSubmissions());
        contestToUpdate.setMaxUserSubmissions(contestDTO.getMaxUserSubmissions());
        contestToUpdate.setStartDate(contestDTO.getStartDate());
        contestToUpdate.setEndDate(contestDTO.getEndDate());

        log.info("{}: Updating contest", this.getClass().getName());
        return contestRepository.save(contestToUpdate);
    }

    @Transactional
    public Contest updateCategoriesOfContest(UUID id, List<Category> categories) {

        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));

        final List<Category> updatedCategoryList = new ArrayList<>();

        for (Category category : categories) {
            final Category categoryIn = categoryRepository.findById(category.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Category was not found with ID: " + category.getId()));
            updatedCategoryList.add(categoryIn);
        }

        contestToUpdate.setCategories(updatedCategoryList);
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


}