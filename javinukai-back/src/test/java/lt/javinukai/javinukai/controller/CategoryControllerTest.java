package lt.javinukai.javinukai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.javinukai.javinukai.dto.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class CategoryControllerTest {

    @Mock
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testCreateCategory() throws Exception {

        CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryName("testCategoryName")
                .description("testCategoryDescription")
                .totalSubmissions(99)
                .build();

        Category category = Category.builder()
                .categoryName("testCategoryName")
                .description("testCategoryDescription")
                .totalSubmissions(99)
                .build();

        Mockito.when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(categoryDTO.getCategoryName()))
                .andExpect(jsonPath("$.description").value(categoryDTO.getDescription()));

    }
}