package lt.javinukai.javinukai.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
