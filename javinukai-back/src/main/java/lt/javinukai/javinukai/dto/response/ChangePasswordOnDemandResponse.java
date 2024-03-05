package lt.javinukai.javinukai.dto.response;

import lombok.*;
import lt.javinukai.javinukai.entity.Category;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordOnDemandResponse {
    private HttpStatus httpStatus;
    private String message;
}
