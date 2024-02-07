package lt.javinukai.javinukai.dto.request.auth;


import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "EMAIL_FORMAT_INCORRECT")
    @Length(max = 100, message = "EMAIL_LENGTH_EXCEEDED")
    private String email;
    private String password;
}
