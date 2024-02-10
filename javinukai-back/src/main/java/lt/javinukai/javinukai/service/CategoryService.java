package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.mapper.CategoryMapper;
import lt.javinukai.javinukai.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryDTO categoryDTO) {

        final List<Category> categories = categoryRepository.findAll();
        for (Category c : categories) {
            if (c.getCategoryName().equals(categoryDTO.getCategoryName()) &&
                    c.getDescription().equals(categoryDTO.getDescription()) &&
                    (c.getTotalSubmissions() == categoryDTO.getTotalSubmissions())
            ) {
                return null;
            }
        }

        final Category category = CategoryMapper.categoryDTOToCategory(categoryDTO);
        final Category createCategory = categoryRepository.save(category);
        log.info("{}: Created and added new category to database", this.getClass().getName());
        return createCategory;
    }

    public List<Category> retrieveAllCategories(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        final Page<Category> pageListCategories = categoryRepository.findAll(pageable);
        final List<Category> listCategories = pageListCategories.getContent();
        log.info("{}: Retrieving all category list from database", this.getClass().getName());
        return listCategories;
    }

    public Category retrieveCategory(UUID id) {
        final Category categoryToShow = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category was not found with ID: " + id));
        log.info("Retrieving category from database, name - {}", this.getClass().getName());
        return categoryToShow;
    }

    public Category updateCategory(UUID id, CategoryDTO categoryDTO) {
        final Category categoryToUpdate = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category was not found with ID: " + id));
        categoryToUpdate.setCategoryName(categoryDTO.getCategoryName());
        categoryToUpdate.setTotalSubmissions(categoryDTO.getTotalSubmissions());
        log.info("{}: Updating category", this.getClass().getName());
        return categoryRepository.save(categoryToUpdate);
    }

    public void deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            log.info("{}: Deleted category from the database with ID: {}", this.getClass().getName(), id);
        } else {
            throw new EntityNotFoundException("Category was not found with ID: " + id);
        }
    }

}
