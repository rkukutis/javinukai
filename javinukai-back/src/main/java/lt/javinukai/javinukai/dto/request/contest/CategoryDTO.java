package lt.javinukai.javinukai.dto.request.contest;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.enums.PhotoSubmissionType;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class CategoryDTO {

    private UUID id;

    @NonNull
    @NotBlank
    @Length(max = 100, message = "CATEGORY_NAME_LENGTH_EXCEEDED")
    private String categoryName;

    @Length(max = 1000, message = "CATEGORY_DESCRIPTION_LENGTH_EXCEEDED")
    private String description;

    private List<Category> contests;

    @NonNull
    @Min(value = 1, message = "AT_LEAST_ONE")
    @Max(value = 9999999, message = "TOO_MANY_SUBMISSIONS")
    private long totalSubmissions;

    private PhotoSubmissionType type;

    private List<String> uploadedPhotos;

}
