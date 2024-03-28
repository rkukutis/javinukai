package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.JwtService;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.dto.request.user.CreateNewUserRequest;
import lt.javinukai.javinukai.dto.response.AuthenticationResponse;
import lt.javinukai.javinukai.dto.request.auth.LoginRequest;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.dto.response.ChangePasswordOnDemandResponse;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.entity.UserToken;
import lt.javinukai.javinukai.enums.TokenType;
import lt.javinukai.javinukai.exception.*;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserTokenService userTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Value("${app.constants.user-defaults.max-photos.total}")
    private int defaultMaxTotal;
    @Value("${app.constants.user-defaults.max-photos.single}")
    private int defaultMaxSinglePhotos;
    @Value("${app.constants.user-defaults.max-photos.collection}")
    private int defaultMaxCollections;


    public User register(UserRegistrationRequest userRegistrationRequest, boolean isRegisteredByAdmin) {
        if (userRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("USER_ALREADY_EXISTS_ERROR");
        }
        log.info("Manual user registration request by admin, enabling user on creation");
        log.info("Registering new user: {}", userRegistrationRequest);
        var user = User.builder()
                .name(userRegistrationRequest.getName())
                .surname(userRegistrationRequest.getSurname())
                .email(userRegistrationRequest.getEmail())
                .phoneNumber(userRegistrationRequest.getPhoneNumber())
                .birthYear(userRegistrationRequest.getBirthYear())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .role(UserRole.USER)
                .customLimits(false)
                .maxTotal(defaultMaxTotal)
                .maxSinglePhotos(defaultMaxSinglePhotos)
                .maxCollections(defaultMaxCollections)
                .isNonLocked(true)
                .isEnabled(isRegisteredByAdmin)
                .institution(userRegistrationRequest.getInstitution())
                .isFreelance(userRegistrationRequest.getInstitution() == null)
                .build();
        User createdUser = userRepository.save(user);
        sendEmailWithToken(createdUser, TokenType.EMAIL_CONFIRM);
        return createdUser;
    }

    public User createUser(CreateNewUserRequest creationRequest, boolean isRegisteredByAdmin) {
        if (userRepository.findByEmail(creationRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("USER_ALREADY_EXISTS_ERROR");
        }
        log.info("Manual user creation done by admin, enabling user on creation");
        log.info("Creating new user: {}", creationRequest);
        User createdUser = User.builder()
                .name(creationRequest.getName())
                .surname(creationRequest.getSurname())
                .email(creationRequest.getEmail())
                .phoneNumber(creationRequest.getPhoneNumber())
                .birthYear(creationRequest.getBirthYear())
                .password(passwordEncoder.encode(creationRequest.getPassword()))
//                .role(UserRole.USER)
                .role(creationRequest.getRole())
                .customLimits(false)
                .maxTotal(defaultMaxTotal)
                .maxSinglePhotos(defaultMaxSinglePhotos)
                .maxCollections(defaultMaxCollections)
                .isNonLocked(true)
                .isEnabled(isRegisteredByAdmin)
                .institution(creationRequest.getInstitution())
                .isFreelance(creationRequest.getInstitution() == null)
                .build();
        return userRepository.save(createdUser);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));
        User user = (User) auth.getPrincipal();
        log.info("Logged in user {}", user.toString());
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
            log.info("Confirming email for {}", user);
        } else {
            throw new InvalidTokenException();
        }
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user with email " + email));
        List<UserToken> userTokens = userTokenService.findAllUserTokens(user.getId(), TokenType.PASSWORD_RESET);
        if (!userTokens.isEmpty() && ZonedDateTime.now().isBefore(userTokens.get(0).getExpiresAt())) {
                throw new TooManyRequestsException("User " + user.getEmail() +
                        " has already received a valid reset token." +
                        " Please wait for an token to expire before requesting another one");
        }
        log.info("Sending password reset email to {}", user);
        sendEmailWithToken(user, TokenType.PASSWORD_RESET);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userTokenService.getTokenUser(token, TokenType.PASSWORD_RESET);
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordResetException("New password is the same as the old one");
        }
        if (userTokenService.tokenIsValid(token, TokenType.PASSWORD_RESET)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            log.info("Setting new password {} for {}", newPassword, user);
            userRepository.save(user);
        } else {
            throw new InvalidTokenException();
        }
    }

    public void changePassword(String token, String newPassword) {
        User user = userTokenService.getTokenUser(token, TokenType.PASSWORD_RESET);
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordResetException("New password is the same as the old one");
        }
        if (userTokenService.tokenIsValid(token, TokenType.PASSWORD_RESET)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            log.info("Setting new password {} for {}", newPassword, user);
            userRepository.save(user);
        } else {
            throw new InvalidTokenException();
        }
    }

    public ChangePasswordOnDemandResponse changePasswordOmDemand(User user, String newPassword, String oldPassword) {

        HttpStatus httpStatus;
        String responseMessage;

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            httpStatus = HttpStatus.CONFLICT;
            responseMessage = "Old password is incorrect";
            return new ChangePasswordOnDemandResponse(httpStatus, responseMessage);
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            httpStatus = HttpStatus.NOT_ACCEPTABLE;
            responseMessage = "New password is the same as the old one";
            return new ChangePasswordOnDemandResponse(httpStatus, responseMessage);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        log.info("Setting new password {} for user: {}", newPassword, user.getUsername());
        userRepository.save(user);

        httpStatus = HttpStatus.OK;
        responseMessage = "Password has been changed";
        return new ChangePasswordOnDemandResponse(httpStatus, responseMessage);
    }

    private void sendEmailWithToken(User user, TokenType type) {
        String tokenValue = userTokenService.createTokenForUser(user, type);
        if (type == TokenType.EMAIL_CONFIRM){
            emailService.sendEmailConfirmation(user, tokenValue);
        } else if (type == TokenType.PASSWORD_RESET) {
            emailService.sendPasswordResetToken(user, tokenValue);
        }
    }
}
