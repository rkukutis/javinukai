package lt.javinukai.javinukai.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDTO extends RegistrationDTO{

    @NotNull
    @Min(0)
    private Integer maxSinglePhotos;
    @NotNull
    @Min(0)
    private Integer maxCollections;
}
