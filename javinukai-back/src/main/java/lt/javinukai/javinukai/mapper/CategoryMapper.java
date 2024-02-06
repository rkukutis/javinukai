package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.CategoryDTO;
import lt.javinukai.javinukai.entity.Category;

public class CategoryMapper {

    public static Category categoryDTOToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.getName())
                .totalSubmissions(categoryDTO.getTotalSubmissions())
                .build();
    }
}
