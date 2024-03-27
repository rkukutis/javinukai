package lt.javinukai.javinukai.config;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.entity.*;
import lt.javinukai.javinukai.enums.ParticipationRequestStatus;
import lt.javinukai.javinukai.enums.PhotoSubmissionType;
import lt.javinukai.javinukai.repository.*;
import lt.javinukai.javinukai.service.CompetitionRecordService;
import lt.javinukai.javinukai.service.ParticipationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CompetitionRecordConfig {

    private final ContestRepository contestRepository;
    private final CategoryRepository categoryRepository;
    private final CompetitionRecordRepository competitionRecordRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompetitionRecordService recordService;
    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestService requestService;

    @Bean
    public CommandLineRunner competitionCreator() {
        return runner -> {
            final Category newCategory = createCategory();
            final Contest newContest = createContestAndCategory();
            final Contest updatedFinalContest = assignCategoryToExistingContest(newContest, newCategory);
            final User userToParticipate = createNewUser();
            createApprovedParticipationRequest(updatedFinalContest.getId(), userToParticipate.getId());
            recordService.createUsersCompetitionRecords(updatedFinalContest.getId(), userToParticipate.getId());
        };
    }


    private void createApprovedParticipationRequest(UUID contestId, UUID userId) {
        Contest tempContest = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException("Contest was not found with ID: " + contestId));
        User tempUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User was not found with Id: " + userId));
        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .requestStatus(ParticipationRequestStatus.ACCEPTED)
                .build();
        ParticipationRequest savedRequest = requestRepository.save(participationRequest);
        savedRequest.setContest(tempContest);
        savedRequest.setUser(tempUser);
        requestRepository.save(participationRequest);
    }

    private User createNewUser() {
        final User userToCreate = User.builder()
                .email("jdoe@mail.com")
                .phoneNumber("+37067598078")
                .name("John")
                .surname("Doe")
                .birthYear(1960)
                .isEnabled(true)
                .customLimits(true)
                .isFreelance(true)
                .isNonLocked(true)
                .role(UserRole.ADMIN)
                .password(passwordEncoder.encode("password"))
                .maxTotal(50)
                .maxSinglePhotos(30)
                .maxCollections(6)
                .build();
        return userRepository.save(userToCreate);
    }

    private Contest assignCategoryToExistingContest(Contest contestToUpdate, Category categoryToAdd) {
        contestToUpdate.addCategory(categoryToAdd);
        return contestRepository.save(contestToUpdate);
    }

    private Contest createContestAndCategory() {

        final Category categoryToCreate01 = Category.builder()
                .name("Morbi sit amet sodales ante")
                .description("Etiam sollicitudin iaculis erat eu ornare. Aliquam erat volutpat. Mauris interdum est vitae leo egestas bibendum. Phasellus sodales molestie mauris at blandit. Morbi sit amet sodales ante, ac iaculis libero. Nulla a mattis ante, quis hendrerit turpis. Quisque magna turpis")
                .maxTotalSubmissions(100)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        final Category categoryToCreate02 = Category.builder()
                .name("Etiam scelerisque scelerisque quam")
                .description("Sed ac arcu ultricies, maximus tortor eget, tincidunt ante. Morbi non finibus neque, ac dapibus tellus. Cras nec fringilla nulla, id sagittis dui. Sed eget luctus neque. Etiam scelerisque scelerisque quam sit amet ullamcorper. Nullam eros purus, mollis sed interdum")
                .maxTotalSubmissions(100)
                .maxUserSubmissions(6)
                .type(PhotoSubmissionType.COLLECTION)
                .build();

        final Contest contestToCreate = Contest.builder()
                .name("Sed dignissim eget ipsum quis feugiat")
                .description("Suspendisse euismod sollicitudin tellus in porta. Praesent faucibus elit eu arcu mattis, ac vehicula massa tincidunt. In auctor mi rhoncus nisl ultrices, a posuere elit pharetra. Aenean tempor arcu at orci aliquam, et pharetra sapien dapibus. Pellentesque volutpat dolor lectus. Proin venenatis, nulla in rutrum egestas, arcu tellus viverra ipsum, eget condimentum risus diam ut ante. Aliquam volutpat viverra risus, eget sodales augue dignissim in. Nulla nulla elit, gravida at placerat eu, tristique vel lacus. Duis a nisi id orci scelerisque bibendum posuere ut leo. Integer fringilla enim at rhoncus congue. Integer viverra quis ipsum non consectetur. Aenean egestas id lorem in dictum. ")
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now().plusDays(5))
                .maxTotalSubmissions(500)
                .maxUserSubmissions(50)
                .build();
        contestToCreate.addCategory(categoryToCreate01);
        contestToCreate.addCategory(categoryToCreate02);
        return contestRepository.save(contestToCreate);
    }

    private Category createCategory() {
        final Category categoryToCreate01 = Category.builder()
                .name("Morbi consectetur in massa nec scelerisque")
                .description("Integer vel risus nulla. Aenean quis vulputate felis, eget sagittis magna. Duis id lacus dignissim, egestas turpis at, interdum enim. Morbi consectetur in massa nec scelerisque. Vivamus viverra, ante vel varius condimentum, est sem gravida turpis, eu imperdiet purus ex. ")
                .maxTotalSubmissions(100)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        final Category savedCategory = categoryRepository.save(categoryToCreate01);
        return savedCategory;
    }
}
