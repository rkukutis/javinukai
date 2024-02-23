package lt.javinukai.javinukai.config;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class CompetitionConfig {

    private final ContestRepository contestRepository;
    private final CategoryRepository categoryRepository;
    private final CompetitionRecordRepository competitionRecordRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CompetitionConfig(ContestRepository contestRepository,
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
            final List<CompetitionRecord> enterCompetitionWithDefaultLimits =
                    placeRecord(userToParticipate, updatedFinalContest);
        };
    }

    private List<CompetitionRecord> placeRecord(User user, Contest contest) {

        final List<CompetitionRecord> competitionRecordList = new ArrayList<>();

        long initialPhotosCount;

        for (int i = 0; i < contest.getCategories().size(); i++) {

            Category currentCategory = contest.getCategories().get(i);
            initialPhotosCount = currentCategory.getTotalSubmissions();

            CompetitionRecord competitionRecordToCreate = CompetitionRecord.builder()
                    .userID(user.getUuid())
                    .userName(user.getName())
                    .contestID(contest.getId())
                    .contestName(contest.getContestName())
                    .maxPhotos(initialPhotosCount)
//                    .maxPhotos(user.getMaxSinglePhotos())
//                    .maxPhotos(2)
                    .categoryID(currentCategory.getId())
                    .categoryName(currentCategory.getCategoryName())
                    .build();
            competitionRecordToCreate.addPhotos(
                    Arrays.asList("url1", "url2", "url3"),
                    competitionRecordToCreate.getMaxPhotos());
            final CompetitionRecord savedCompetitionRecord = competitionRecordRepository.save(competitionRecordToCreate);

            log.info("competition record saved, id -> {}", savedCompetitionRecord.getId());
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
        log.info("saving user...");
        final User savedUser = userRepository.save(userToCreate);

        final String userInfo = String.format("user created, full name -> %s %s, email -> %s ",
                savedUser.getName(),
                savedUser.getSurname(),
                savedUser.getEmail());
        log.info(userInfo);

        return savedUser;
    }

    private Contest assignCategoryToExistingContest(Contest contestToUpdate, Category categoryToAdd) {
        contestToUpdate.addCategory(categoryToAdd);
        final Contest updatedContest = contestRepository.save(contestToUpdate);
        log.info("contest was updated");
        return updatedContest;
    }

    private Contest createContestAndCategory() {

        final Category categoryToCreate01 = Category.builder()
                .categoryName("įvykiai")
                .description("pokyčiai, patraukę akį")
                .totalSubmissions(40)
                .build();
        String messageForCategory = String.format("category was created, name -> %s, id -> none",
                categoryToCreate01.getCategoryName());
        log.info(messageForCategory);

        final Category categoryToCreate02 = Category.builder()
                .categoryName("tech")
                .description("technologijos. keičiančios gyvenimą")
                .totalSubmissions(35)
                .build();
        log.info(messageForCategory);

        final Contest contestToCreate = Contest.builder()
                .contestName("pro objektyvą - 2023")
                .description("gražiauisios 2023-ųjų akimirkos")
                .totalSubmissions(20)
                .build();
        contestToCreate.addCategory(categoryToCreate01);
        contestToCreate.addCategory(categoryToCreate02);
        final Contest savedContest = contestRepository.save(contestToCreate);

        final String messageForContest = String.format("contest was saved, name -> %s, id -> %s",
                savedContest.getContestName(),
                savedContest.getId());
        log.info(messageForContest);

        return  savedContest;
    }

    private Category createCategory() {
        final Category categoryToCreate01 = Category.builder()
                .categoryName("pasaulio šalys")
                .description("už gimtinės ribų")
                .totalSubmissions(30)
                .build();
        final Category savedCategory = categoryRepository.save(categoryToCreate01);
        final String messageForCategory = String.format("category was saved, name -> %s, id -> %s",
                savedCategory.getCategoryName(),
                savedCategory.getId());
        log.info(messageForCategory);
        return savedCategory;
    }
}
