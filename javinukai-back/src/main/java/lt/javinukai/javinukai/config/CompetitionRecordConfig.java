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
import java.util.Arrays;
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
                    .contest(contest)
                    .user(user)
                    .maxPhotos(currentCategory.getTotalSubmissions())
                    .build();
            final CompetitionRecord savedCompetitionRecord = competitionRecordRepository
                    .save(competitionRecordToCreate);

            savedCompetitionRecord.addPhotos(Arrays.asList("url1", "url2", "url3"), savedCompetitionRecord.getMaxPhotos());

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
                .categoryName("įvykiai")
                .description("pokyčiai, patraukę akį")
                .totalSubmissions(40)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        final Category categoryToCreate02 = Category.builder()
                .categoryName("tech")
                .description("technologijos. keičiančios gyvenimą")
                .totalSubmissions(35)
                .type(PhotoSubmissionType.COLLECTION)
                .build();

        final Contest contestToCreate = Contest.builder()
                .contestName("pro objektyvą - 2023")
                .description("gražiauisios 2023-ųjų akimirkos")
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
                .categoryName("pasaulio šalys")
                .description("už gimtinės ribų")
                .totalSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        final Category savedCategory = categoryRepository.save(categoryToCreate01);
        return savedCategory;
    }
}
