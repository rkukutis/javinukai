package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;

public class CategoryMapper {

    public static Category categoryDTOToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .categoryName(categoryDTO.getCategoryName())
                .description(categoryDTO.getDescription())
                .totalSubmissions(categoryDTO.getTotalSubmissions())
                .uploadedPhoto(categoryDTO.getUploadedPhoto())
                .build();
    }
}
