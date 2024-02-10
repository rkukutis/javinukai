package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.mapper.ContestMapper;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Contest createContest(ContestDTO contestDTO) {
        final Contest contest = ContestMapper.contestDTOToContest(contestDTO);
        final Contest createdContest = contestRepository.save(contest);
        log.info("{}: Created and added new contest to database", this.getClass().getName());
        return createdContest;
    }

    public List<Contest> retrieveAllContests() {
        final List<Contest> listContests = contestRepository.findAll();
        log.info("{}: Retrieving all contest list from database", this.getClass().getName());
        return listContests;
    }

    public Contest retrieveContest(UUID id) {
        final Contest contestToShow = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        log.info("Retrieving contest from database, name - {}", this.getClass().getName());
        return contestToShow;
    }

    public Contest updateContest(UUID id, ContestDTO contestDTO) {
        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        contestToUpdate.setContestName(contestDTO.getContestName());
        contestToUpdate.setDescription(contestDTO.getDescription());
        contestToUpdate.setTotalSubmissions(contestDTO.getTotalSubmissions());
        contestToUpdate.setStartDate(contestDTO.getStartDate());
        contestToUpdate.setEndDate(contestDTO.getEndDate());
        log.info("{}: Updating contest", this.getClass().getName());
        return contestRepository.save(contestToUpdate);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// v1 //
////////
    public Contest addCategories(UUID id, List<UUID> categories) {

        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        for (UUID categoryID : categories) {
            final Category category = categoryRepository.findById(categoryID).orElseThrow(
                    () -> new EntityNotFoundException("Contest was not found with ID: " + id));
            contestToUpdate.addCategory(category);
            category.addContest(contestToUpdate);
            log.info("{}: Added category - {}", this.getClass().getName(), category.getClass().getName());
        }
        return contestRepository.save(contestToUpdate);
    }

    public Contest removeCategories(UUID id, List<UUID> categories) {

        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        for (UUID categoryID : categories) {
            final Category category = categoryRepository.findById(categoryID).orElseThrow(
                    () -> new EntityNotFoundException("Contest was not found with ID: " + id));
            contestToUpdate.removeCategory(category);
            category.removeContest(contestToUpdate);
            log.info("{}: Removed category - {}", this.getClass().getName(), category.getClass().getName());
        }
        return contestRepository.save(contestToUpdate);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// v2 //
////////
    public Contest updateCategoriesOfContest(UUID id, List<Category> categories) {

        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));

        final List<Category> updatedCategoryList = new ArrayList<>();
        for (Category category : categories) {
            final Category categoryIn = categoryRepository.findById(category.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Contest was not found with ID: " + id));
            updatedCategoryList.add(categoryIn);
        }

        contestToUpdate.setCategories(updatedCategoryList);
        return contestRepository.save(contestToUpdate);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteContest(UUID id) {
        if (contestRepository.existsById(id)) {
            contestRepository.deleteById(id);
            log.info("{}: Deleted contest from the database with ID: {}", this.getClass().getName(), id);
        } else {
            throw new EntityNotFoundException("Contest was not found with ID: " + id);
        }
    }

}