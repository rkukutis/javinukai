package lt.javinukai.javinukai.dto.request.auth;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordResetRequest {

    private String resetToken;
    private String newPassword;
}
