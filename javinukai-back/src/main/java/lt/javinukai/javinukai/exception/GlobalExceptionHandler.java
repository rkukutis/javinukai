package lt.javinukai.javinukai.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception, WebRequest request) {
        log.debug(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}
