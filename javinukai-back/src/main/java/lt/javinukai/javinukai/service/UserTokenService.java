package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.entity.UserToken;
import lt.javinukai.javinukai.enums.TokenType;
import lt.javinukai.javinukai.exception.InvalidTokenException;
import lt.javinukai.javinukai.repository.UserRepository;
import lt.javinukai.javinukai.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    @Value("${app.constants.security.email-confirm-token.valid-minutes}")
    private int emailConfirmTime;

    @Value("${app.constants.security.password-reset-token.valid-minutes}")
    private int passwordResetTime;


    public void createTokenForUser(User user, String value, TokenType type) {
        // TODO: hash tokens before storing in DB
        int validMinutes = type == TokenType.EMAIL_CONFIRM ? emailConfirmTime : passwordResetTime;
        UserToken token = new UserToken(value, type, user, validMinutes);
        UserToken createdToken = userTokenRepository.save(token);
        user.addToken(createdToken);
        userRepository.save(user);
    }

    public boolean tokenIsValid(String tokenValue, TokenType type) {
        Optional<UserToken> tokenOptional = userTokenRepository.findByTokenValueAndType(tokenValue, type);
        if (tokenOptional.isEmpty()) return false;
        UserToken token = tokenOptional.get();
        if (!userRepository.existsById(token.getUser().getUuid())) return false;
        return token.getExpiresAt().isBefore(ZonedDateTime.now());
    }
    public User getTokenUser(String tokenValue, TokenType type) {
        UserToken token = userTokenRepository.findByTokenValueAndType(tokenValue, type)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        return userRepository.findById(token.getUser().getUuid())
                .orElseThrow(() -> new InvalidTokenException("No user associated with supplied token"));
    }
}
