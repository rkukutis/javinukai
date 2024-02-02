package lt.javinukai.javinukai.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Error responses should be per RFC 7807 specification

    @ExceptionHandler({UserNotFoundException.class})
    public ProblemDetail handleUserNotFound(UserNotFoundException exception, WebRequest request) {
        log.debug(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle("USER_NOT_FOUND_ERROR");
        res.setDetail(exception.getMessage());
        return res;
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ProblemDetail handleUserNotFound(UserAlreadyExistsException exception, WebRequest request) {
        log.debug(exception.getMessage());
        ProblemDetail res = ProblemDetail.forStatus(400);
        res.setTitle(exception.getMessage());
        res.setDetail("An user with provided email already exists!");
        return res;
    }
}
