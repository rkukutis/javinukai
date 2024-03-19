package lt.javinukai.javinukai.dto.request.contest;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestDTO {

    private UUID id;

    @NonNull
    @NotBlank
    @Length(max = 100, message = "CONTEST_NAME_LENGTH_EXCEEDED")
    private String name;

    @NonNull
    @NotBlank
    @Length(max = 1000, message = "CONTEST_DESCRIPTION_LENGTH_EXCEEDED")
    private String description;

    @Setter
    private List<Category> categories;

    @NonNull
    @Min(value = 1, message = "AT_LEAST_ONE")
    @Max(value = 9999999, message = "TOO_MANY_SUBMISSIONS")
    private long maxTotalSubmissions;

    @NonNull
    @Min(value = 1, message = "AT_LEAST_ONE")
    @Max(value = 9999999, message = "TOO_MANY_SUBMISSIONS")
    private long maxUserSubmissions;

    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
