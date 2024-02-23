package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.request.contest.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;

public class CategoryMapper {

    public static Category categoryDTOToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .categoryName(categoryDTO.getCategoryName())
                .description(categoryDTO.getDescription())
                .totalSubmissions(categoryDTO.getTotalSubmissions())
                .uploadedPhotos(categoryDTO.getUploadedPhotos())
                .type(categoryDTO.getType())
                .build();
    }

    public static CategoryDTO categoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .totalSubmissions(category.getTotalSubmissions())
                .type(category.getType())
                .build();
    }
}
