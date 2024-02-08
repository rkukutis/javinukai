package lt.javinukai.javinukai.service;

import lt.javinukai.javinukai.dto.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService underTest;

    @Test
    public void createCategoryInService() {

        final CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryName("testCategoryName")
                .description("testCategoryDescription")
                .totalSubmissions(99)
                .build();

        final Category categoryEntity = Category.builder()
                .categoryName("testCategoryName")
                .description("testCategoryDescription")
                .totalSubmissions(99)
                .build();

        when(categoryRepository.save(any(Category.class))).thenReturn(categoryEntity);
//        when(categoryRepository.save(eq(categoryEntity))).thenReturn(categoryEntity);

        final Category result = underTest.createCategory(categoryDTO);

        assertNotNull(categoryEntity);
        assertNotNull(result);
        assertEquals(result, categoryEntity);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

}