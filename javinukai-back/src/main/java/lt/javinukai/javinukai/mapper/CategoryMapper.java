package lt.javinukai.javinukai.mapper;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CategoryDTO;
import lt.javinukai.javinukai.dto.response.CategoryWithEntriesResponse;
import lt.javinukai.javinukai.entity.Category;

@Slf4j
public class CategoryMapper {

    public static Category categoryDTOToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .maxTotalSubmissions(categoryDTO.getMaxTotalSubmissions())
                .maxUserSubmissions(categoryDTO.getMaxUserSubmissions())
                .uploadedPhotos(categoryDTO.getUploadedPhotos())
                .type(categoryDTO.getType())
                .build();
    }

    public static CategoryDTO categoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .maxUserSubmissions(category.getMaxUserSubmissions())
                .maxTotalSubmissions(category.getMaxTotalSubmissions())
                .name(category.getName())
                .description(category.getDescription())
                .type(category.getType())
                .uploadedPhotos(category.getUploadedPhotos())
                .build();
    }

    public static CategoryWithEntriesResponse categoryToCategoryWithEntries(Category inputCategory, int totalEntries) {
        return CategoryWithEntriesResponse.builder()
                .id(inputCategory.getId())
                .createdAt(inputCategory.getCreatedAt())
                .modifiedAt(inputCategory.getModifiedAt())
                .totalEntries(totalEntries)
                .name(inputCategory.getName())
                .description(inputCategory.getDescription())
                .maxTotalSubmissions(inputCategory.getMaxTotalSubmissions())
                .maxUserSubmissions(inputCategory.getMaxUserSubmissions())
                .uploadedPhotos(inputCategory.getUploadedPhotos())
                .type(inputCategory.getType())
                .build();
    }
}
