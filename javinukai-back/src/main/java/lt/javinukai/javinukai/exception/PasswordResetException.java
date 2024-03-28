package lt.javinukai.javinukai.exception;

public class PasswordResetException extends RuntimeException{
    public PasswordResetException() {
    }

    public PasswordResetException(String message) {
        super(message);
    }
}
