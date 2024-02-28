package lt.javinukai.javinukai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.JwtService;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.dto.response.UserParticipationResponse;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.PhotoSubmissionType;
import lt.javinukai.javinukai.service.CompetitionRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompetitionRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Slf4j
class CompetitionRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompetitionRecordService competitionRecordService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private UserParticipationResponse userParticipationResponse;

    @BeforeEach
    public void init() {
        final User user = User.builder()
                .email("bensullivan@mail.com")
                .phoneNumber("+37047812482")
                .name("Ben")
                .surname("Sullivan")
                .birthYear(1960)
                .isEnabled(true)
                .isFreelance(true)
                .isNonLocked(true)
                .role(UserRole.USER)
                .password(passwordEncoder.encode("password"))
                .maxTotal(10)
                .maxSinglePhotos(10)
                .maxCollections(10)
                .build();

        final Category category = Category.builder()
                .categoryName("įvykiai")
                .description("pokyčiai, patraukę akį")
                .totalSubmissions(40)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        final Contest contest = Contest.builder()
                .contestName("pro objektyvą - 2023")
                .description("gražiauisios 2023-ųjų akimirkos")
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .totalSubmissions(20)
                .build();

        this.userParticipationResponse = UserParticipationResponse.builder()
                .records(null)
                .message("")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    @Test
    public void addUserToContestReturnsRecord() throws Exception{

        UUID contestID = UUID.randomUUID();
        UUID userID = UUID.randomUUID();

        given(competitionRecordService.createUsersCompetitionRecords(contestID, userID))
                .willReturn(userParticipationResponse);

        ResultActions response = mockMvc.perform(post("/api/v1/records")
                .param("contestID", contestID.toString())
                .param("userID", userID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()));
    }




}

























