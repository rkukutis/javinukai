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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

@Configuration
@RequiredArgsConstructor
public class StartupUsers {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    @Bean
    CommandLineRunner initialize() {
        return args -> {
            List<List<String>> records = new ArrayList<>();
            try (Scanner scanner = new Scanner(new File("src/main/resources/test-data/users.csv"))) {
                while (scanner.hasNextLine()) {
                    records.add(getRecordFromLine(scanner.nextLine()));
                }
            }
            for(List<String> user : records) {
                User createdUser = User.builder()
                        .name(user.get(0))
                        .surname(user.get(1))
                        .email(user.get(2))
                        .institution(user.get(3))
                        .isFreelance(Boolean.parseBoolean(user.get(4)))
                        .maxTotal(Integer.parseInt(user.get(5)))
                        .maxSinglePhotos(Integer.parseInt(user.get(6)))
                        .maxCollections(Integer.parseInt(user.get(7)))
                        .role(UserRole.valueOf(user.get(8)))
                        .isEnabled(Boolean.parseBoolean(user.get(9)))
                        .isNonLocked(Boolean.parseBoolean(user.get(10)))
                        .phoneNumber(user.get(11))
                        .birthYear(Integer.parseInt(user.get(12)))
                        .password(passwordEncoder.encode(user.get(0)))
                        .build();
                userRepository.save(createdUser);
            }
        };
    }

    @Bean
    CommandLineRunner createDirectories() {
        return args -> {
            Path root = Path.of("src/main/resources/images");
            if (!Files.exists(root)) Files.createDirectories(root);
            for (ImageSize size : ImageSize.values()) {
                Path storagePathParent = Path.of(root.toString(), size.localStoragePath);
                if (!Files.exists(storagePathParent)) Files.createDirectory(storagePathParent);
            }
        };
    }
}
