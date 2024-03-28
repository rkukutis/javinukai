package lt.javinukai.javinukai.dto.request.collection;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
public class CollectionUpdateRequest {

    @Length(max = 100, message = "TITLE_LENGTH_EXCEEDED")
    private String newName;
    @NotBlank
    @Length(max = 1000, message = "DESCRIPTION_LENGTH_EXCEEDED")
    private String newDescription;


}
