package lt.javinukai.javinukai.config;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
public class StartupData {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initialize() {
        return args -> {

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

        };
    }

    @Bean
    CommandLineRunner createDirectories() {
        return args -> {
            Path root = Path.of("src/main/resources/images");
            if (!Files.exists(root)) Files.createDirectory(root);
            for (ImageSize size : ImageSize.values()) {
                Path storagePathParent = Path.of(root.toString(), size.localStoragePath);
                if (!Files.exists(storagePathParent)) Files.createDirectory(storagePathParent);
            }
        };
    }
}
