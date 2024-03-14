package lt.javinukai.javinukai.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lt.javinukai.javinukai.config.security.UserRole;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@ToString
public class CreateNewUserRequest {

    @NotBlank
    @Length(max = 100, message = "FIRST_NAME_LENGTH_EXCEEDED")
    private String name;
    @NotBlank
    @Length(max = 100, message = "LAST_NAME_LENGTH_EXCEEDED")
    private String surname;
    @NotNull
    @Min(value = 1900, message = "AGE_INVALID")
    @Max(value = 2100, message = "AGE_INVALID")
    private Integer birthYear;
    @NotBlank
    @Pattern(regexp = "(^\\+?\\d{9,11})", message = "INVALID_PHONE_FORMAT")
    @Length(max = 12, message = "PHONE_NUMBER_LENGTH_EXCEEDED")
    private String phoneNumber;
    @NotBlank
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "EMAIL_FORMAT_INCORRECT")
    @Length(max = 100, message = "EMAIL_LENGTH_EXCEEDED")
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private UserRole role;
    @Length(max = 100, message = "INSTITUTION_NAME_LENGTH_EXCEEDED")
    private String institution;
}
