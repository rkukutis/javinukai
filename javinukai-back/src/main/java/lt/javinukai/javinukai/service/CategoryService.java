package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CategoryDTO;
import lt.javinukai.javinukai.dto.response.CategoryCreationResponse;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.mapper.CategoryMapper;
import lt.javinukai.javinukai.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ContestService contestService;


    @Transactional
    public CategoryCreationResponse createCategory(CategoryDTO categoryDTO) {

        Category categoryInRepo = categoryRepository.findByNameAndDescriptionAndTotalSubmissions(
                categoryDTO.getName(), categoryDTO.getDescription(), categoryDTO.getTotalSubmissions());

        HttpStatus httpStatus;
        String message;

        if (categoryInRepo != null) {
            log.info("{}: Category already exists in the database", this.getClass().getName());
            httpStatus = HttpStatus.OK;
            message = "Request for category creation completed, already in repo";
            return new CategoryCreationResponse(categoryInRepo, httpStatus, message);
        }

        Category category = CategoryMapper.categoryDTOToCategory(categoryDTO);
        final Category createCategory = categoryRepository.save(category);
        log.info("{}: Created and added new category to database", this.getClass().getName());
        httpStatus = HttpStatus.CREATED;
        message = String.format("Request for category creation completed, given ID: %s", category.getId());
        return new CategoryCreationResponse(createCategory, httpStatus,message);
    }

    public Page<Category> retrieveAllCategories(Pageable pageable, String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            log.info("{}: Retrieving all categories list from database", this.getClass().getName());
            return categoryRepository.findAll(pageable);
        } else {
            log.info("{}: Retrieving categories by name", this.getClass().getName());
            return categoryRepository.findByName(keyword, pageable);
        }
    }

    public Category retrieveCategory(UUID id) {
        final Category categoryToShow = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category was not found with ID: " + id));
        log.info("Retrieving category from database, name - {}", this.getClass().getName());
        return categoryToShow;
    }

    @Transactional
    public Category updateCategory(UUID id, CategoryDTO categoryDTO) {
        final Category categoryToUpdate = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category was not found with ID: " + id));
        categoryToUpdate.setName(categoryDTO.getName());
        categoryToUpdate.setDescription(categoryDTO.getDescription());
        categoryToUpdate.setTotalSubmissions(categoryDTO.getTotalSubmissions());
        categoryToUpdate.setType(categoryDTO.getType());
        log.info("{}: Updating category", this.getClass().getName());
        return categoryRepository.save(categoryToUpdate);
    }

    @Transactional
    public void deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            log.info("{}: Deleted category from the database with ID: {}", this.getClass().getName(), id);
        } else {
            throw new EntityNotFoundException("Category was not found with ID: " + id);
        }
    }

    public List<Category> retrieveContestCategories(UUID contestId) {
        return contestService.retrieveContest(contestId).getCategories();
    }
}
