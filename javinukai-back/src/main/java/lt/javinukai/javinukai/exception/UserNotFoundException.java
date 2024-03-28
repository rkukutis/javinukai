package lt.javinukai.javinukai.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
    }

    public UserNotFoundException(UUID userId) {
        super(String.format("User %s not found!", userId.toString()));
    }

    public UserNotFoundException(String message) {
        super(String.format(message));
    }
}
