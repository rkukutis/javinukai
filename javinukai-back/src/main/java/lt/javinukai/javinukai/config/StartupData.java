package lt.javinukai.javinukai.config;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StartupData {
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner initialize() {
        return args -> {
            User testUser = User.builder()
                    .name("John")
                    .surname("Doe")
                    .birthYear(1984)
                    .email("jdoe@mail.com")
                    .maxSinglePhotos(10)
                    .maxCollections(10)
                    .phoneNumber("+37047812482")
                    .institution("Delfi")
                    .isFreelance(false)
                    .build();
            userRepository.save(testUser);
        };
    }
}
