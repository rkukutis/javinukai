package lt.javinukai.javinukai.service;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.JwtService;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.dto.response.ChangePasswordOnDemandResponse;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.TokenType;
import lt.javinukai.javinukai.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings()
@Slf4j
class AuthenticationServiceTest {
    @Mock
    public PasswordEncoder mockPasswordEncoder;
    @Mock
    public UserRepository mockUserRepository;
    @Mock
    public UserTokenService mockTokenService;
    @Mock
    public EmailService mockEmailService;
    @Mock
    public JwtService mockJwtService;
    @InjectMocks
    public AuthenticationService authenticationService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;

    @Test
    void register() {
        UserRegistrationRequest testRegister = UserRegistrationRequest.builder()
                .name("Test")
                .surname("McTest")
                .birthYear(1976)
                .email("mctest@mail.com")
                .phoneNumber("+37067515483")
                .institution(null)
                .password("password")
                .build();

//        when(mockPasswordEncoder.encode(anyString())).thenReturn("ENCODED_PASSWORD");
//        when(mockTokenService.createTokenForUser(any(User.class), any(TokenType.class))).thenReturn("EMAIL_TOKEN");
//        User createdUser = authenticationService.register(testRegister, false);
//        verify(mockPasswordEncoder, times(1)).encode(anyString());
//        System.out.println(createdUser);
    }

    @Test
    void login() {
    }

    @Test
    void confirmEmail() {
    }

    @Test
    void forgotPassword() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void whenInvalidOldPassword_thenReturnConflictStatus() {
        String oldPassword = "incorrect_old_password";
        String newPassword = "new_password";

        User user = User.builder()
                .email("testuser")
                .password(passwordEncoder.encode("correct_old_password"))
                .build();

        ChangePasswordOnDemandResponse response = authenticationService.changePasswordOmDemand(user, newPassword, oldPassword);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getHttpStatus());
        assertEquals("Old password is incorrect", response.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenNewPasswordSameAsOld_thenReturnNotAcceptableStatus() {
        String oldPassword = "correct_old_password";
        String newPassword = "correct_old_password";

        User user = User.builder()
                .email("testuser")
                .password(passwordEncoder.encode(oldPassword))
                .build();

        ChangePasswordOnDemandResponse response = authenticationService.changePasswordOmDemand(user, newPassword, oldPassword);

        assertNotNull(response);
        assertEquals("Old password is incorrect", response.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
}