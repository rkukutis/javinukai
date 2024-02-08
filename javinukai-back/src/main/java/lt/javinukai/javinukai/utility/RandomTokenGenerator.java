package lt.javinukai.javinukai.utility;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class RandomTokenGenerator {

    private RandomTokenGenerator() {
    }

    public static String generateToken(int length) {
        StringBuilder builder = new StringBuilder();
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int sizeChars = chars.length();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(sizeChars);
            builder.append(chars.charAt(index));
        }
        log.debug("Generating {} symbol random token", length);
        return builder.toString();
    }
}
