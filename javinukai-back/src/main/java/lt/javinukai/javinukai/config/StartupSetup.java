package lt.javinukai.javinukai.config;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class StartupSetup {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContestRepository contestRepository;
    private final CategoryRepository categoryRepository;

    @Bean
    public CommandLineRunner setup() throws IOException {
        return (args) -> {
            categoryAndContestSetup();
            directorySetup();
            userSetup();
        };
    }


    private void categoryAndContestSetup() {
        createContestAndCategories();
        createCategory();
        addCategoryToExistingContest(createIncompleteContestAndCategories());
    }

    private void directorySetup() throws IOException {
        Path root = Path.of("src/main/resources/images");
        if (!Files.exists(root)) {
            Files.createDirectories(root);
            for (ImageSize size : ImageSize.values()) {
                Path storagePathParent = Path.of(root.toString(), size.localStoragePath);
                if (!Files.exists(storagePathParent)) Files.createDirectory(storagePathParent);
            }
        }
    }

    private void userSetup() {
        User admin = User.builder()
                .name("John")
                .surname("Doe")
                .email("jdoe@mail.com")
                .institution("LRT")
                .isFreelance(false)
                .maxSinglePhotos(10)
                .maxCollections(10)
                .role(UserRole.ADMIN)
                .isEnabled(true)
                .isNonLocked(true)
                .phoneNumber("+37047812482")
                .birthYear(1984)
                .password(passwordEncoder.encode("password"))
                .build();

        User user = User.builder()
                .name("John")
                .surname("Doe")
                .email("user@mail.com")
                .institution("LRT")
                .isFreelance(false)
                .maxSinglePhotos(10)
                .maxCollections(10)
                .role(UserRole.USER)
                .isEnabled(true)
                .isNonLocked(true)
                .phoneNumber("+37047812482")
                .birthYear(1984)
                .password(passwordEncoder.encode("password"))
                .build();

        userRepository.saveAll(List.of(admin, user));
    }

    private void createCategory() {
        Category category01 = Category.builder()
                .categoryName("gamta")
                .description("ne mūsų kurta aplinka")
                .totalSubmissions(100)
                .build();
        categoryRepository.save(category01);
    }

    private void addCategoryToExistingContest(Contest incompleteContest) {
        Category category01 = Category.builder()
                .categoryName("asmenys")
                .description("gyvenimiška patirtis")
                .totalSubmissions(100)
                .build();
        incompleteContest.addCategory(category01);
        Category category02 = Category.builder()
                .categoryName("asmenys")
                .description("gyvenimiška patirtis")
                .totalSubmissions(555)
                .build();
        incompleteContest.addCategory(category02);
        contestRepository.save(incompleteContest);
    }

    private Contest createIncompleteContestAndCategories() {

        Category category01 = Category.builder()
                .categoryName("knyga")
                .description("spausdintas žodis")
                .totalSubmissions(100)
                .build();

        Category category02 = Category.builder()
                .categoryName("mokyklos klasė")
                .description("dažnam pažįstama")
                .totalSubmissions(50)
                .build();

        Contest contest01 = Contest.builder()
                .contestName("išmintis")
                .description("nes žinai")
                .totalSubmissions(888)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        contest01.addCategory(category01);
        contest01.addCategory(category02);

        contestRepository.save(contest01);
        return contest01;
    }

    private void createContestAndCategories() {

        Category category01 = Category.builder()
                .categoryName("medicina")
                .description("slaugos mokslas")
                .totalSubmissions(100)
                .build();

        Category category02 = Category.builder()
                .categoryName("sportas")
                .description("citius, altius, fortius")
                .totalSubmissions(20)
                .build();

        Category category03 = Category.builder()
                .categoryName("istorija")
                .description("daugiau, nei kaulai ir griuvėsiai")
                .totalSubmissions(500)
                .build();

        Contest contest01 = Contest.builder()
                .contestName("viltis")
                .description("paskutinė, nepabėgusi")
                .totalSubmissions(777)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        contest01.addCategory(category01);
        contest01.addCategory(category02);
        contest01.addCategory(category03);

        contestRepository.save(contest01);
    }
}
