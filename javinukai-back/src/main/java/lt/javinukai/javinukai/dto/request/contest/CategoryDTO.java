package lt.javinukai.javinukai.dto.request.contest;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lt.javinukai.javinukai.entity.Category;
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

    @NotBlank
    @Length(max = 100, message = "CATEGORY_NAME_LENGTH_EXCEEDED")
    private String name;

    @Length(max = 1000, message = "CATEGORY_DESCRIPTION_LENGTH_EXCEEDED")
    private String description;

    private List<Category> contests;

    @Min(value = 1, message = "AT_LEAST_ONE")
    @Max(value = 9999999, message = "TOO_MANY_SUBMISSIONS")
    private long maxTotalSubmissions;

    @Min(value = 1, message = "AT_LEAST_ONE")
    @Max(value = 9999999, message = "TOO_MANY_SUBMISSIONS")
    private long maxUserSubmissions;

    @NotNull
    private PhotoSubmissionType type;

    private List<String> uploadedPhotos;
}
