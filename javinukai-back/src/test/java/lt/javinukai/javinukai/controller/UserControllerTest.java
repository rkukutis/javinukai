package lt.javinukai.javinukai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private UserRegistrationRequest testRegistration;


    @BeforeEach
    public void setupRegistration() {
        this.testRegistration = new UserRegistrationRequest(
                "Fester",
                "McTester",
                1990,
                "+370147684214",
                "mctester@mail.com",
                "LRT");
    }

    @Test
    void givenRegistrationRequest_createNewUser() throws Exception {

        // 1) happy case
        MvcResult result1 = mockMvc.perform(getBuilderForPost(testRegistration)).andReturn();
        assertEquals(200, result1.getResponse().getStatus());
        User createdUser = objectMapper
                .readValue(result1.getResponse().getContentAsString(StandardCharsets.UTF_8), User.class);
        assertNotNull(createdUser.getUuid());
        assertNotNull(createdUser.getCreatedAt());
        assertFalse(createdUser.getIsFreelance());
        // 2) invalid email
        testRegistration.setEmail("MAIL");
        MvcResult result2 = mockMvc.perform(getBuilderForPost(testRegistration)).andReturn();
        assertEquals(400, result2.getResponse().getStatus());
    }

    @Test
    void givenDeleteRequest_whenUserExists_deleteUser() throws Exception {
        MvcResult result1 = mockMvc.perform(getBuilderForPost(testRegistration)).andReturn();
        assertEquals(200, result1.getResponse().getStatus());
        User createdUser = objectMapper
                .readValue(result1.getResponse().getContentAsString(StandardCharsets.UTF_8), User.class);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/" + createdUser.getUuid().toString())
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void givenDeleteRequest_whenUserDoesNotExits_return404() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/" + UUID.randomUUID())
        ).andReturn();

        assertEquals(404, result.getResponse().getStatus());
    }

    private RequestBuilder getBuilderForPost(UserRegistrationRequest userRegistrationRequest) throws JsonProcessingException {
        return MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistrationRequest));
    }

}