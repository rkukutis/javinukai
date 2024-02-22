package lt.javinukai.javinukai.dto.response;

import lombok.Builder;
import lombok.Getter;
import lt.javinukai.javinukai.entity.User;

@Builder
@Getter
public class AuthenticationResponse {
    private final String token;
    private final User user;
}
