package lt.javinukai.javinukai.config;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.PhotoSubmissionType;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class CompetitionRecordConfig {

    private final ContestRepository contestRepository;
    private final CategoryRepository categoryRepository;
    private final CompetitionRecordRepository competitionRecordRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CompetitionRecordConfig(ContestRepository contestRepository,
                                   CategoryRepository categoryRepository,
                                   CompetitionRecordRepository competitionRecordRepository,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        this.contestRepository = contestRepository;
        this.categoryRepository = categoryRepository;
        this.competitionRecordRepository = competitionRecordRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner competitionCreator() {
        return runner -> {
            final Category newCategory = createCategory();
            final Contest newContest = createContestAndCategory();
            final Contest updatedFinalContest = assignCategoryToExistingContest(newContest, newCategory);
            final User userToParticipate = createNewUser();
            final List<CompetitionRecord> competitionRecordsWithDefaultLimits =
                    placeRecord(userToParticipate, updatedFinalContest);
//            final List<CompetitionRecord> updateCompetitionRecord = deleteRecord(competitionRecordsWithDefaultLimits);

        };
    }

    private List<CompetitionRecord> deleteRecord(List<CompetitionRecord> enterCompetitionRecordWithDefaultLimits) {

        final List<CompetitionRecord> competitionRecordList = enterCompetitionRecordWithDefaultLimits;
        competitionRecordRepository.deleteById(competitionRecordList.get(0).getId());
        competitionRecordList.remove(competitionRecordList.get(0));
        return competitionRecordList;
    }

    private List<CompetitionRecord> placeRecord(User user, Contest contest) {

        final List<CompetitionRecord> competitionRecordList = new ArrayList<>();

        for (Category currentCategory : contest.getCategories()) {

            final CompetitionRecord competitionRecordToCreate = CompetitionRecord.builder()
                    .category(currentCategory)
                    .entries(null)
                    .contest(contest)
                    .user(user)
                    .maxPhotos(currentCategory.getTotalSubmissions())
                    .build();
            final CompetitionRecord savedCompetitionRecord = competitionRecordRepository
                    .save(competitionRecordToCreate);

            competitionRecordRepository.save(savedCompetitionRecord);
            competitionRecordList.add(savedCompetitionRecord);
        }

        return competitionRecordList;
    }

    private User createNewUser() {
        final User userToCreate = User.builder()
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
        final User savedUser = userRepository.save(userToCreate);

        return savedUser;
    }

    private Contest assignCategoryToExistingContest(Contest contestToUpdate, Category categoryToAdd) {
        contestToUpdate.addCategory(categoryToAdd);
        final Contest updatedContest = contestRepository.save(contestToUpdate);
        return updatedContest;
    }

    private Contest createContestAndCategory() {

        final Category categoryToCreate01 = Category.builder()
                .name("Morbi sit amet sodales ante")
                .description("Etiam sollicitudin iaculis erat eu ornare. Aliquam erat volutpat. Mauris interdum est vitae leo egestas bibendum. Phasellus sodales molestie mauris at blandit. Morbi sit amet sodales ante, ac iaculis libero. Nulla a mattis ante, quis hendrerit turpis. Quisque magna turpis")
                .totalSubmissions(40)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        final Category categoryToCreate02 = Category.builder()
                .name("Etiam scelerisque scelerisque quam")
                .description("Sed ac arcu ultricies, maximus tortor eget, tincidunt ante. Morbi non finibus neque, ac dapibus tellus. Cras nec fringilla nulla, id sagittis dui. Sed eget luctus neque. Etiam scelerisque scelerisque quam sit amet ullamcorper. Nullam eros purus, mollis sed interdum")
                .totalSubmissions(35)
                .type(PhotoSubmissionType.COLLECTION)
                .build();

        final Contest contestToCreate = Contest.builder()
                .name("Sed dignissim eget ipsum quis feugiat")
                .description("Suspendisse euismod sollicitudin tellus in porta. Praesent faucibus elit eu arcu mattis, ac vehicula massa tincidunt. In auctor mi rhoncus nisl ultrices, a posuere elit pharetra. Aenean tempor arcu at orci aliquam, et pharetra sapien dapibus. Pellentesque volutpat dolor lectus. Proin venenatis, nulla in rutrum egestas, arcu tellus viverra ipsum, eget condimentum risus diam ut ante. Aliquam volutpat viverra risus, eget sodales augue dignissim in. Nulla nulla elit, gravida at placerat eu, tristique vel lacus. Duis a nisi id orci scelerisque bibendum posuere ut leo. Integer fringilla enim at rhoncus congue. Integer viverra quis ipsum non consectetur. Aenean egestas id lorem in dictum. ")
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .totalSubmissions(20)
                .build();
        contestToCreate.addCategory(categoryToCreate01);
        contestToCreate.addCategory(categoryToCreate02);
        final Contest savedContest = contestRepository.save(contestToCreate);

        return  savedContest;
    }

    private Category createCategory() {
        final Category categoryToCreate01 = Category.builder()
                .name("Morbi consectetur in massa nec scelerisque")
                .description("Integer vel risus nulla. Aenean quis vulputate felis, eget sagittis magna. Duis id lacus dignissim, egestas turpis at, interdum enim. Morbi consectetur in massa nec scelerisque. Vivamus viverra, ante vel varius condimentum, est sem gravida turpis, eu imperdiet purus ex. ")
                .totalSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        final Category savedCategory = categoryRepository.save(categoryToCreate01);
        return savedCategory;
    }
}
