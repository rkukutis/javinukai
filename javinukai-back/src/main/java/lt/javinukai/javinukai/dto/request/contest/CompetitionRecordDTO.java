package lt.javinukai.javinukai.dto.request.contest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.util.UUID;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class CompetitionRecordDTO {

    private UUID userID;
    private UUID categoryID;
    private UUID contestID;

    @Min(value = 1, message = "AT_LEAST_ONE")
    @Max(value = 9999999, message = "TOO_MANY_SUBMISSIONS")
    private long maxPhotos;
}
