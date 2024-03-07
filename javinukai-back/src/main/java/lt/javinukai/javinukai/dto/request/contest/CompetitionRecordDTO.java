package lt.javinukai.javinukai.dto.request.contest;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class CompetitionRecordDTO {

    @Min(value = 1, message = "AT_LEAST_ONE")
    @Max(value = 9999999, message = "TOO_MANY_SUBMISSIONS")
    private long maxPhotos;
}
