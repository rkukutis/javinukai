package lt.javinukai.javinukai.config.security;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.dto.response.AuthenticationResponse;
import lt.javinukai.javinukai.dto.request.auth.LoginRequest;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.TokenType;
import lt.javinukai.javinukai.exception.InvalidTokenException;
import lt.javinukai.javinukai.exception.UserAlreadyExistsException;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.repository.UserRepository;
import lt.javinukai.javinukai.service.EmailService;
import lt.javinukai.javinukai.service.UserTokenService;
import lt.javinukai.javinukai.utility.RandomTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserTokenService userTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.constants.user-defaults.max-photos.single}")
    private int defaultMaxSinglePhotos;

    @Value("${app.constants.user-defaults.max-photos.collection}")
    private int defaultMaxCollections;

    public AuthenticationResponse register(UserRegistrationRequest userRegistrationRequest) {

        if (userRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("USER_ALREADY_EXISTS_ERROR");
        }

        var user = User.builder()
                .name(userRegistrationRequest.getName())
                .surname(userRegistrationRequest.getSurname())
                .email(userRegistrationRequest.getEmail())
                .phoneNumber(userRegistrationRequest.getPhoneNumber())
                .birthYear(userRegistrationRequest.getBirthYear())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .role(UserRole.USER)
                .maxSinglePhotos(defaultMaxSinglePhotos)
                .maxCollections(defaultMaxCollections)
                .isNonLocked(true)
                .isEnabled(false)
                .institution(userRegistrationRequest.getInstitution())
                .isFreelance(userRegistrationRequest.getInstitution() == null)
                .build();
        User createdUser = userRepository.save(user);
        sendEmailWithToken(user, TokenType.EMAIL_CONFIRM);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(createdUser)
                .build();

    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Wrong Credentials"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }


    public void confirmEmail(String emailConfirmToken) {
        if (userTokenService.tokenIsValid(emailConfirmToken, TokenType.EMAIL_CONFIRM)) {
            User user = userTokenService.getTokenUser(emailConfirmToken, TokenType.EMAIL_CONFIRM);
            user.setIsEnabled(true);
            userRepository.save(user);
        } else {
            throw new InvalidTokenException();
        }
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user with email " + email));
        sendEmailWithToken(user, TokenType.PASSWORD_RESET);
    }

    public void resetPassword(String token, String newPassword) {
        if (userTokenService.tokenIsValid(token, TokenType.PASSWORD_RESET)) {
            User user = userTokenService.getTokenUser(token, TokenType.PASSWORD_RESET);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new InvalidTokenException();
        }
    }

    private void sendEmailWithToken(User user, TokenType type) {
        String tokenValue = RandomTokenGenerator.generateToken(64); // TODO: remove magic number
        userTokenService.createTokenForUser(user, tokenValue, type);
        if (type == TokenType.EMAIL_CONFIRM){
            emailService.sendEmailConfirmation(user, tokenValue);
        } else if (type == TokenType.PASSWORD_RESET) {
            emailService.sendPasswordResetToken(user, tokenValue);
        }
    }
}
