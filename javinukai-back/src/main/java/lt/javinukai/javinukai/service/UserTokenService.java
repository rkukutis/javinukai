package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.entity.UserToken;
import lt.javinukai.javinukai.enums.TokenType;
import lt.javinukai.javinukai.exception.InvalidTokenException;
import lt.javinukai.javinukai.repository.UserRepository;
import lt.javinukai.javinukai.repository.UserTokenRepository;
import lt.javinukai.javinukai.utility.RandomTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserTokenService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final Argon2PasswordEncoder tokenEncoder;

    @Value("${app.constants.security.token.email-confirm-token.valid-minutes}")
    private int emailConfirmTime;

    @Value("${app.constants.security.token.password-reset-token.valid-minutes}")
    private int passwordResetTime;

    @Value("${app.constants.security.token.length}")
    private int tokenLength;

    public String createTokenForUser(User user, TokenType type) {
        String tokenValue = RandomTokenGenerator.generateToken(tokenLength);

        int validMinutes = type == TokenType.EMAIL_CONFIRM ? emailConfirmTime : passwordResetTime;
        UserToken token = new UserToken(tokenEncoder.encode(tokenValue), type, user, validMinutes);
        UserToken createdToken = userTokenRepository.save(token);
        log.info("Creating new {} token for {}", type, user);
        user.addToken(createdToken);
        userRepository.save(user);
        return tokenValue;
    }

    public boolean tokenIsValid(String tokenValue, TokenType type) {

        Optional<UserToken> tokenOptional = userTokenRepository
                .findByTokenValueAndType(tokenEncoder.encode(tokenValue), type);
        if (tokenOptional.isEmpty()) return false;
        UserToken token = tokenOptional.get();
        if (!userRepository.existsById(token.getUser().getId())) return false;
        boolean isValid = ZonedDateTime.now().isBefore(token.getExpiresAt());
        if(isValid){
            token.setExpiresAt(ZonedDateTime.now());
            userTokenRepository.save(token);
        }
        return isValid;
    }
    public User getTokenUser(String tokenValue, TokenType type) {
        String hashedReceivedToken = tokenEncoder.encode(tokenValue);
        UserToken token = userTokenRepository.findByTokenValueAndType(hashedReceivedToken, type)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        User user =  userRepository.findById(token.getUser().getId())
                .orElseThrow(() -> new InvalidTokenException("No user associated with supplied token"));
        log.info("Returning {} for {} token", user, type);
        return user;
    }

    public List<UserToken> findAllUserTokens(UUID userID, TokenType type) {
        return userTokenRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userID, type);
    }
}
