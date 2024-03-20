package lt.javinukai.javinukai.dto.response;

import lombok.*;
import lt.javinukai.javinukai.entity.PastCompetitionRecord;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchivingResponse {
    private List<PastCompetitionRecord> pastCompetitionRecords;
    private HttpStatus httpStatus;
    private String message;
}
