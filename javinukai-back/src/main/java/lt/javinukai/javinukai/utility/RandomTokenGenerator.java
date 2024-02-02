package lt.javinukai.javinukai.utility;

import java.security.SecureRandom;

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
        return builder.toString();
    }
}
