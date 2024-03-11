package lt.javinukai.javinukai.mapper;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;

@Slf4j
public class CategoryMapper {

    public static Category categoryDTOToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .totalSubmissions(categoryDTO.getTotalSubmissions())
                .uploadedPhotos(categoryDTO.getUploadedPhotos())
                .type(categoryDTO.getType())
                .build();
    }

    public static CategoryDTO categoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .totalSubmissions(category.getTotalSubmissions())
                .type(category.getType())
                .build();
    }
}
