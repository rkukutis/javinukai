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
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import lt.javinukai.javinukai.service.CompetitionRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = CompetitionRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Slf4j
class CompetitionRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CompetitionRecordRepository competitionRecordRepository;

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private UserRepository userRepository;

    @MockBean
    private CompetitionRecordService competitionRecordService;

    @Autowired
    CompetitionRecordController competitionRecordController;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private UserParticipationResponse userParticipationResponse;
    private User user;
    private Category category;
    private Contest contest;

    @BeforeEach
    public void init() {



        this.user = User.builder()
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

        this.category = Category.builder()
                .id(UUID.randomUUID())
                .name("įvykiai")
                .description("pokyčiai, patraukę akį")
                .maxTotalSubmissions(40)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        this.contest = Contest.builder()
                .id(UUID.randomUUID())
                .name("pro objektyvą - 2023")
                .description("gražiauisios 2023-ųjų akimirkos")
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .maxTotalSubmissions(20)
                .build();

        this.userParticipationResponse = UserParticipationResponse.builder()
                .records(null)
                .message("")
                .httpStatus(HttpStatus.OK)
                .build();
    }

}