package lt.javinukai.javinukai.exception;

public class ContestNotFoundException extends RuntimeException {
    public ContestNotFoundException(String message) {
        super(message);
    }

    public ContestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContestNotFoundException(Throwable cause) {
        super(cause);
    }
}
