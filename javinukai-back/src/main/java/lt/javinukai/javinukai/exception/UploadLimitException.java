package lt.javinukai.javinukai.exception;

public class UploadLimitException extends RuntimeException{

    public UploadLimitException() {
    }

    public UploadLimitException(String message) {
        super(message);
    }
}
