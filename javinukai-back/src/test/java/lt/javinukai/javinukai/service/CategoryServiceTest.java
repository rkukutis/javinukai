package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.mapper.CategoryMapper;
import lt.javinukai.javinukai.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

//    @Test
//    public void createCategoryReturnsCategory() {
//
//        final CategoryDTO categoryDTO = CategoryDTO.builder()
//                .categoryName("test category name")
//                .description("testCategory description")
//                .totalSubmissions(66)
//                .build();
//
//        final UUID id = UUID.randomUUID();
//        final Category category = new Category(id,
//                categoryDTO.getCategoryName(),
//                categoryDTO.getDescription(),
//                categoryDTO.getTotalSubmissions(),
//                null, null, null, null);
//
//        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
//        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);
//
//        final Category createdCategory = categoryService
//                .createCategory(CategoryMapper.categoryToCategoryDTO(category));
//
//        Assertions.assertThat(createdCategory).isNotNull();
//        Assertions.assertThat(createdCategory.getId()).isNotNull();
//    }

//    @Test
//    public void createCategoryReturnsNull() {
//
//        CategoryDTO categoryDTO = CategoryDTO.builder()
//                .categoryName("test category name")
//                .description("testCategory description")
//                .totalSubmissions(66)
//                .build();
//        Category category = CategoryMapper.categoryDTOToCategory(categoryDTO);
//        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
//
//        final Category createdCategory = categoryService.createCategory(categoryDTO);
//
//        Assertions.assertThat(createdCategory).isNull();
//    }

    @Test
    public void retrieveAllCategoriesReturnsListOfCategories() {

        final Page<Category> pageOfCategories = Mockito.mock(Page.class);
        when(categoryRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageOfCategories);

        final Page<Category> categoriesToDisplay = categoryService.retrieveAllCategories(1, 2);

        Assertions.assertThat(categoriesToDisplay).isNotNull();
    }

    @Test
    public void retrieveCategoryReturnsCategory() {

        final UUID id = UUID.randomUUID();
        final Category category = new Category(id,
                "test category name",
                "testCategory description",
                66,
                null, null, null, null);

        when(categoryRepository.findById(id)).thenReturn(Optional.ofNullable(category));
        final Category createdCategory = categoryService.retrieveCategory(id);

        Assertions.assertThat(createdCategory).isNotNull();
        Assertions.assertThat(createdCategory.getId()).isNotNull();
    }

    @Test
    public void retrieveCategoryReturnsNotFound() {

        UUID idWrong = UUID.randomUUID();

        when(categoryRepository.findById(idWrong)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.retrieveCategory(idWrong);
        });

        Assertions.assertThat(exception.getMessage())
                .isEqualTo("Category was not found with ID: " + idWrong);
    }

    @Test
    public void updateCategoryReturnsCategory() {

        final CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryName("test category name")
                .description("testCategory description")
                .totalSubmissions(66)
                .build();

        final UUID id = UUID.randomUUID();
        final Category category = new Category(id,
                categoryDTO.getCategoryName(),
                categoryDTO.getDescription(),
                categoryDTO.getTotalSubmissions(),
                null, null, null, null);

        when(categoryRepository.findById(id)).thenReturn(Optional.ofNullable(category));
        when(categoryRepository.save(category)).thenReturn(category);
        final Category updatedCategory = categoryService.updateCategory(id, categoryDTO);

        Assertions.assertThat(updatedCategory).isNotNull();
        Assertions.assertThat(updatedCategory.getId()).isNotNull();
    }

    @Test
    public void deleteCategorySuccess() {

        final UUID id = UUID.randomUUID();
        final Category category = new Category(id,
                "test category name",
                "testCategory description",
                66,
                null, null, null, null);

        when(categoryRepository.existsById(id)).thenReturn(true);

        assertAll(() -> categoryService.deleteCategory(id));
    }

    @Test
    public void deleteCategoryFail() {

        final UUID id = UUID.randomUUID();
        final Category category = new Category(id,
                "test category name",
                "testCategory description",
                66,
                null, null, null, null);

        when(categoryRepository.existsById(id)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.deleteCategory(id);
        });

        Assertions.assertThat(exception.getMessage()).
                isEqualTo("Category was not found with ID: " + id);
    }
}