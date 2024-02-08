package lt.javinukai.javinukai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.mapper.CategoryMapper;
import lt.javinukai.javinukai.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(path = "/categories")
    public ResponseEntity<Category> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        final Category createdCategory = categoryService.createCategory(categoryDTO);
        log.info("Request for category creation completed, given ID: {}", createdCategory.getId());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping(path = "/categories")
    public ResponseEntity<List<Category>> retrieveAllCategories() {
        final List<Category> categoryList = categoryService.retrieveAllCategories();
        log.info("Request for retrieving all categories, {} record(s) found", categoryList.size());
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}")
    public ResponseEntity<Category> retrieveCategory(@PathVariable @NotNull UUID id) {
        log.info("Request for retrieving category with ID: {}", id);
        final Category category = categoryService.retrieveCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping(path = "/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable @NotNull UUID id, @RequestBody @Valid CategoryDTO categoryDTO) {
        log.info("Request for updating category with ID: {}", id);
        final Category categoryToUpdate = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(categoryToUpdate, HttpStatus.OK);
    }

    @DeleteMapping(path = "/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable @NotNull UUID id) {
        log.info("Request for deleting category with ID: {}", id);
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
