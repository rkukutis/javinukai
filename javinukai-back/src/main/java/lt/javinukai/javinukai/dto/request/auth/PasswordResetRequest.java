package lt.javinukai.javinukai.dto.request.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PasswordResetRequest {
    @NotBlank
    private String resetToken;
    @NotBlank
    private String newPassword;
}
