package lt.javinukai.javinukai.dto.response;

import lombok.*;
import lt.javinukai.javinukai.entity.PastCompetition;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchivingResponse {
    private PastCompetition pastCompetition;
    private HttpStatus httpStatus;
    private String message;
}
