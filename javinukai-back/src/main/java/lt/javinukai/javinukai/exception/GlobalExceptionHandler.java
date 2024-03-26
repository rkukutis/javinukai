package lt.javinukai.javinukai.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Error responses should be per RFC 7807 specification

    @ExceptionHandler({UserNotFoundException.class})
    public ProblemDetail handleUserNotFound(UserNotFoundException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("USER_NOT_FOUND_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    // This works as a generic "x not found" error handler
    @ExceptionHandler({EntityNotFoundException.class})
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("ENTITY_NOT_FOUND_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle(exception.getMessage());
        res.setDetail("An user with provided email already exists!");
        return res;
    }
    @ExceptionHandler({InvalidTokenException.class})
    public ProblemDetail handleInvalidToken(InvalidTokenException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("INVALID_TOKEN_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({TooManyRequestsException.class})
    public ProblemDetail handleTooManyRequests(TooManyRequestsException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(429);
        res.setTitle("TOO_MANY_REQUESTS_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({IOException.class})
    public ProblemDetail handleIOException(IOException exception) {
        log.error(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(500);
        res.setTitle("SERVER_IO_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ProblemDetail handleTooManyRequests(MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("REQUEST_VALIDATION_ERROR");
        res.setDetail(Objects.requireNonNull(exception.getDetailMessageArguments())[1].toString());
        return res;
    }

    @ExceptionHandler({PasswordResetException.class})
    public ProblemDetail handlePasswordResetException(PasswordResetException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("PASSWORD_RESET_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({ImageDeleteException.class})
    public ProblemDetail handlePhotoDeletionError(ImageDeleteException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(500);
        res.setTitle("PHOTO_DELETION_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({ImageProcessingException.class})
    public ProblemDetail handleImageProcessingError(ImageProcessingException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("PHOTO_PROCESSING_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({ImageValidationException.class})
    public ProblemDetail handleImageValidationError(ImageValidationException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("PHOTO_VALIDATION_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({UploadLimitException.class})
    public ProblemDetail handleLimitError(UploadLimitException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("UPLOAD_LIMIT_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({JsonProcessingException.class})
    public ProblemDetail handleJSONParseError(JsonProcessingException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("JSON_DESERIALIZATION_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({ContestExpiredException.class})
    public ProblemDetail handleExpiredContestError(ContestExpiredException exception) {
        log.warn(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("EXPIRED_CONTEST_ERROR_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }
}
