package lt.javinukai.javinukai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.dto.request.auth.ForgotPasswordRequest;
import lt.javinukai.javinukai.dto.request.auth.LoginRequest;
import lt.javinukai.javinukai.dto.request.auth.PasswordResetRequest;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.entity.UserToken;
import lt.javinukai.javinukai.enums.TokenType;
import lt.javinukai.javinukai.repository.UserRepository;
import lt.javinukai.javinukai.repository.UserTokenRepository;
import lt.javinukai.javinukai.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@SpringBootTest
class AuthorizationControllerTest {
    public MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserTokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;
    @MockBean
    EmailService mockEmailService;

    @Captor
    ArgumentCaptor<String> tokenCaptor;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @Order(1)
    @SneakyThrows
    void whenSubmitValidRegistration_thenCreateDisabledUser() {
        UserRegistrationRequest register = UserRegistrationRequest.builder()
                .name("Rick")
                .surname("Hoopoe")
                .institution("Delfi")
                .phoneNumber("+37024581490")
                .email("rickh@mail.com")
                .birthYear(1998)
                .password("slaptazodis")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
        ).andReturn().getResponse();

        assertNull(response.getCookie("jwt"));
        assertEquals(201, response.getStatus());
        User createdUser = userRepository.findByEmail("rickh@mail.com").get();
        assertNotNull(createdUser);
        assertFalse(createdUser.getIsEnabled());
    }

    @ParameterizedTest
    @Order(2)
    @CsvSource(
            {
                    "Rick,Hoopoe,LRT,+37014862145,badmail,1998,password",
                    "Rick,Hoopoe,LRT,+37014862145,mail@mail.com,1863,password",
                    "Rick,Hoopoe,LRT,+37014862145,mail2@mail.com,9999,password",
                    "Rick,Hoopoe,LRT,+370148621451254758422454415,mail3@mail.co,1998,password",
                    "Rick,Hoopoe,LRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRTLRT,+37014862145,mail@mail.co,1998,password",
            }
    )
    @SneakyThrows
    void whenInvalidRegistration_thenReturn400(String name, String surname, String institution, String phoneNumber,
                                               String email, int birthYear, String password) {
        UserRegistrationRequest register = UserRegistrationRequest.builder()
                .name(name)
                .surname(surname)
                .institution(institution)
                .phoneNumber(phoneNumber)
                .email(email)
                .birthYear(birthYear)
                .password(password)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
        ).andReturn().getResponse();
        assertNull(response.getCookie("jwt"));
        assertEquals(400, response.getStatus());
    }

    @Test
    @SneakyThrows
    @Order(3)
    void whenAccountUnconfirmed_thenReturn403() {

        UserRegistrationRequest register = UserRegistrationRequest.builder()
                .name("John")
                .surname("Wayne")
                .institution("NYT")
                .phoneNumber("+37024581490")
                .email("jwayne@mail.com")
                .birthYear(1998)
                .password("hunter2")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
        ).andReturn();

        LoginRequest login = LoginRequest.builder()
                .email("jwayne@mail.com")
                .password("hunter2")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        ).andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    @SneakyThrows
    @Order(4)
    void whenSubmitConfirmationToken_thenEnableAccount() {
        UserRegistrationRequest register = UserRegistrationRequest.builder()
                .name("Bender")
                .surname("Fender")
                .institution("Slate")
                .phoneNumber("+37024581490")
                .email("fbender@mail.com")
                .birthYear(1998)
                .password("pa$$word")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
        ).andReturn();

        verify(mockEmailService).sendEmailConfirmation(userCaptor.capture(), tokenCaptor.capture());
        System.out.println(tokenCaptor.getValue());

        User user = userRepository.findByEmail("fbender@mail.com").get();
        assertFalse(user.isEnabled());
        assertNotNull(tokenCaptor.getValue());
        assertEquals(user.getId() ,userCaptor.getValue().getId());

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/confirm-email")
                        .param("token", tokenCaptor.getValue())
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
        User enabledUser = userRepository.findByEmail("fbender@mail.com").get();
        assertTrue(enabledUser.isEnabled());
    }


    @Test
    @Order(5)
    void whenValidLogin_thenLogin() throws Exception {

        User enabledUser = User.builder()
                .name("Snake")
                .surname("Plissken")
                .email("splissken@mail.com")
                .password(passwordEncoder.encode("newyork"))
                .phoneNumber("814762145")
                .role(UserRole.USER)
                .birthYear(1966)
                .isFreelance(true)
                .maxCollections(10)
                .maxSinglePhotos(10)
                .isNonLocked(true)
                .isEnabled(true)
                .build();

        userRepository.save(enabledUser);

        LoginRequest login = LoginRequest.builder()
                .email("splissken@mail.com")
                .password("newyork")
                .build();

        MockHttpServletResponse res1 = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        ).andReturn().getResponse();

        assertEquals(200, res1.getStatus());

        UserRegistrationRequest returnedUser = objectMapper.readValue(res1.getContentAsString(), UserRegistrationRequest.class);
        assertEquals("splissken@mail.com", returnedUser.getEmail());
        assertNotNull(res1.getCookie("jwt"));
    }

    @Test
    @Order(6)
    @SneakyThrows
    void whenInvalidLogin_ThenReturn403() {
        LoginRequest login = LoginRequest.builder()
                .email("baduser@mail.com")
                .password("bad_password")
                .build();

        MockHttpServletResponse res1 = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        ).andReturn().getResponse();

        assertEquals(403, res1.getStatus());
        assertNull(res1.getCookie("jwt"));
    }

    @Test
    @Order(7)
    @SneakyThrows
    void whenForgotPassword_thenResetPassword() {
        User enabledUser = User.builder()
                .name("Jeffrey")
                .surname("Lebowski")
                .email("thedude@mail.com")
                .password(passwordEncoder.encode("bowlingbuds"))
                .phoneNumber("814662545")
                .role(UserRole.USER)
                .birthYear(1959)
                .isFreelance(false)
                .institution("Los Angeles Times")
                .maxCollections(10)
                .maxSinglePhotos(10)
                .isNonLocked(true)
                .isEnabled(true)
                .build();

        User testUser = userRepository.save(enabledUser);

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("thedude@mail.com");

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forgotPasswordRequest))
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(mockEmailService).sendPasswordResetToken(userCaptor.capture(), tokenCaptor.capture());
        assertEquals(testUser.getId(), userCaptor.getValue().getId());
        List<UserToken> tokens = tokenRepository
                .findByUserIdAndTypeOrderByCreatedAtDesc(testUser.getId(), TokenType.PASSWORD_RESET);
        assertEquals(1, tokens.size());



        PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                .resetToken(tokenCaptor.getValue())
                .newPassword("moneymoneymoney")
                .build();

        MockHttpServletResponse resetResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordResetRequest))
        ).andReturn().getResponse();
        assertEquals(200, resetResponse.getStatus());

        LoginRequest newLogin = LoginRequest.builder()
                .email("thedude@mail.com")
                .password("moneymoneymoney")
                .build();
        LoginRequest oldLogin = LoginRequest.builder()
                .email("thedude@mail.com")
                .password("bowlingbuds")
                .build();

        MockHttpServletResponse loginResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newLogin))
        ).andReturn().getResponse();
        MockHttpServletResponse oldLoginResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(oldLogin))
        ).andReturn().getResponse();

        assertEquals(200, loginResponse.getStatus());
        assertEquals(403, oldLoginResponse.getStatus());
    }

}