package lt.javinukai.javinukai.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ContestDTO {

    @NonNull
    @NotBlank
    @Length(max = 100, message = "CONTEST_NAME_LENGTH_EXCEEDED")
    private String contestName;

    @NonNull
    @NotBlank
    @Length(max = 1000, message = "CONTEST_DESCRIPTION_LENGTH_EXCEEDED")
    private String description;

//    private List<Category> categories;

    @NonNull
    @NotBlank
    @Min(value = 1, message = "AT_LEAST_ONE")
    @Max(value = 9999999 ,message = "TOO_MANY_SUBMISSIONS")
    private long totalSubmissions;

    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
