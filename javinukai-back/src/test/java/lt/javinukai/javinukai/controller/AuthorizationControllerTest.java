package lt.javinukai.javinukai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lt.javinukai.javinukai.config.security.ApplicationFilters;
import lt.javinukai.javinukai.dto.request.auth.LoginRequest;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.entity.UserToken;
import lt.javinukai.javinukai.enums.TokenType;
import lt.javinukai.javinukai.repository.UserRepository;
import lt.javinukai.javinukai.repository.UserTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@SpringBootTest
class AuthorizationControllerTest {
    public MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    UserTokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

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

        LoginRequest login = new LoginRequest();
        login.setEmail("jwayne@mail.com");
        login.setPassword("hunter2");

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

        User user = userRepository.findByEmail("fbender@mail.com").get();
        assertFalse(user.isEnabled());
        List<UserToken> tokens = tokenRepository
                .findByUserUuidAndTypeOrderByCreatedAtDesc(user.getUuid(), TokenType.EMAIL_CONFIRM);
        assertEquals(1, tokens.size());
        UserToken confirmToken = tokens.get(0);
        assertTrue(confirmToken.getExpiresAt().isAfter(confirmToken.getCreatedAt()));
        assertEquals(user.getUuid(), confirmToken.getUser().getUuid());

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/confirm-email")
                        .param("token", confirmToken.getTokenValue())
        ).andReturn().getResponse();
        assertEquals(200, response.getStatus());
        User enabledUser = userRepository.findByEmail("fbender@mail.com").get();
        assertTrue(enabledUser.isEnabled());
    }


    @Test
    @Order(5)
    void whenValidLogin() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail("jdoe@mail.com");
        login.setPassword("password");

        MockHttpServletResponse res1 = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        ).andReturn().getResponse();

        assertEquals(200, res1.getStatus());

        UserRegistrationRequest returnedUser = objectMapper.readValue(res1.getContentAsString(), UserRegistrationRequest.class);
        assertEquals("jdoe@mail.com", returnedUser.getEmail());
        assertNotNull(res1.getCookie("jwt"));
    }

    @Test
    @Order(6)
    @SneakyThrows
    void whenInvalidLogin_ThenReturn403() {
        LoginRequest login = new LoginRequest();
        login.setEmail("baduser@mail.com");
        login.setPassword("bad_password");

        MockHttpServletResponse res1 = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        ).andReturn().getResponse();

        assertEquals(403, res1.getStatus());
        assertNull(res1.getCookie("jwt"));
    }

}