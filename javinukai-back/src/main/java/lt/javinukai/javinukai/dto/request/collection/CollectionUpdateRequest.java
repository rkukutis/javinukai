package lt.javinukai.javinukai.dto.request.collection;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CollectionUpdateRequest {

    @NotBlank
    private String newName;
    @NotBlank
    private String newDescription;


}
