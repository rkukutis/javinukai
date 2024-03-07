package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.mapper.ContestMapper;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ContestService {

    private final CategoryRepository categoryRepository;
    private final ContestRepository contestRepository;

    @Autowired
    public ContestService(ContestRepository contestRepository, CategoryRepository categoryRepository) {
        this.contestRepository = contestRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Contest createContest(ContestDTO contestDTO) {
//        public Contest createContest(ContestDTO contestDTO, User user) {

        final Contest contest = ContestMapper.contestDTOToContest(contestDTO);
        final Contest createdContest = contestRepository.save(contest);

        final List<Category> categoryList = new ArrayList<>();
        for (Category category : contestDTO.getCategories()) {
            final Category categoryIn = categoryRepository
                    .findByNameAndDescriptionAndTotalSubmissions(
                            category.getName(),
                            category.getDescription(),
                            category.getTotalSubmissions());

                if (categoryIn == null) {
                    throw new EntityNotFoundException("category was not found with ID: " + category.getId());
                }
            categoryList.add(categoryIn);
        }

        createdContest.setCategories(categoryList);
//        createdContest.setCreatedBy(user);
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

    @Transactional
    public Contest updateContest(UUID id, ContestDTO contestDTO) {

        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        contestToUpdate.setName(contestDTO.getName());
        contestToUpdate.setDescription(contestDTO.getDescription());
        contestToUpdate.setTotalSubmissions(contestDTO.getTotalSubmissions());
        contestToUpdate.setStartDate(contestDTO.getStartDate());
        contestToUpdate.setEndDate(contestDTO.getEndDate());

//        final List<Category> updatedCategoryList = new ArrayList<>();
//
//        for (Category category : contestDTO.getCategories()) {
//            final Category categoryIn = categoryRepository.findById(category.getId()).orElseThrow(
//                    () -> new EntityNotFoundException("Category was not found with ID: " + category.getId()));
//            updatedCategoryList.add(categoryIn);
//        }
//
//        contestToUpdate.setCategories(updatedCategoryList);
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

}