package lt.javinukai.javinukai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.JwtService;
import lt.javinukai.javinukai.dto.request.contest.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.mapper.CategoryMapper;
import lt.javinukai.javinukai.service.CategoryService;
import lt.javinukai.javinukai.service.ContestService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Slf4j
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private ContestService contestService;
    @MockBean
    private JwtService jwtService;

    private CategoryDTO categoryDTO;
    private Category category;

    @BeforeEach
    public void init() {
        categoryDTO = CategoryDTO.builder()
                .categoryName("test category name")
                .description("testCategory description")
                .totalSubmissions(66)
                .build();
        category = CategoryMapper.categoryDTOToCategory(categoryDTO);
    }

    @Test
    public void createCategoryReturnsCreated() throws Exception {

        given(categoryService.createCategory(ArgumentMatchers.any()))
                .willReturn(category);

        ResultActions response = mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName", CoreMatchers.is(categoryDTO.getCategoryName())));
    }



    @Test
    public void retrieveAllCategoriesReturnsListOfCategories() throws Exception {

        final Page<Category> page = new PageImpl<>(Collections.singletonList(category));
        final ResponseEntity<Page<Category>> responseEntity = new ResponseEntity<>(page, HttpStatus.OK);

        when(categoryService.retrieveAllCategories(0, 10))
                .thenReturn(page);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories")
                .param("pageNumber", String.valueOf(1))
                .param("pageSize", String.valueOf(10))
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0].categoryName").value(page.getContent().get(0).getCategoryName()));
    }

    @Test
    public void retrieveCategoryReturnsCategory() throws Exception{

        final UUID id = UUID.randomUUID();
        when(categoryService.retrieveCategory(id)).thenReturn(category);

        ResultActions response = mockMvc.perform(get("/api/v1/categories/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName", CoreMatchers.is(categoryDTO.getCategoryName())));
    }

    @Test
    public void updateCategoryReturnsCreated() throws Exception {

        given(categoryService.createCategory(ArgumentMatchers.any()))
                .willReturn(category);

        ResultActions response = mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName", CoreMatchers.is(categoryDTO.getCategoryName())));
    }


}












