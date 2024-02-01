package lt.javinukai.javinukai.dto;

import lombok.Builder;
import lombok.Getter;
import lt.javinukai.javinukai.entity.User;

@Builder
public class AuthenticationResponse {
    @Getter
    private final String token;
    @Getter
    private final User user;
}
