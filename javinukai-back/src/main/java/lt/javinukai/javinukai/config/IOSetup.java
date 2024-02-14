package lt.javinukai.javinukai.config;

import lt.javinukai.javinukai.enums.ImageSize;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class IOSetup {

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
