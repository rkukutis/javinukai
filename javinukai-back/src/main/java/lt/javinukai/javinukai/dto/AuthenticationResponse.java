package lt.javinukai.javinukai.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public class AuthenticationResponse {
    @Getter
    private final String token;
}
