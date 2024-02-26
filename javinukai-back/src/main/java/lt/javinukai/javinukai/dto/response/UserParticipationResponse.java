package lt.javinukai.javinukai.dto.response;

import lombok.*;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserParticipationResponse {
    private List<CompetitionRecord> records;
    private HttpStatus httpStatus;
    private String message;
}
