package lt.javinukai.javinukai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.config.security.AuthenticationService;
import lt.javinukai.javinukai.dto.request.auth.ForgotPasswordRequest;
import lt.javinukai.javinukai.dto.request.auth.PasswordResetRequest;
import lt.javinukai.javinukai.dto.response.AuthenticationResponse;
import lt.javinukai.javinukai.dto.request.auth.LoginRequest;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthorizationController {
    private final AuthenticationService authenticationService;

    @Value("${app.constants.security.jwt-cookie-valid-hours}")
    private int jwtValidTimeHours;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegistrationRequest registration,
                                         HttpServletResponse response, HttpServletRequest request) {
        AuthenticationResponse auth = authenticationService.register(registration);
        String cookieString = getResponseCookie("jwt", auth.getToken(), jwtValidTimeHours).toString();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieString);
        return ResponseEntity.ok().body(auth.getUser());
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest login, HttpServletResponse response) {
        AuthenticationResponse auth = authenticationService.login(login);
        String cookieString = getResponseCookie("jwt", auth.getToken(), jwtValidTimeHours).toString();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieString);
        return ResponseEntity.ok().body(auth.getUser());
    }

    @GetMapping("/test")
    public ResponseEntity<String> testCookie(@CookieValue("jwt") String jwt, Authentication authentication,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok().body(user.getName() + " " + user.getSurname());
    }

    private ResponseCookie getResponseCookie(String name, String value, int hoursValid) {
        return ResponseCookie.from(name, value)
                .maxAge(Duration.ofHours(hoursValid))
                .httpOnly(true)
                .secure(true)
                .sameSite("strict")
                .path("/")
                .build();
    }


    @PostMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam String token) {
        authenticationService.confirmEmail(token);
        return ResponseEntity.ok().body("Email confirmed. You may log in");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequest passwordResetRequest) {
       authenticationService.resetPassword(passwordResetRequest.getResetToken(), passwordResetRequest.getNewPassword());
       return ResponseEntity.ok().body("Password has been reset");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        authenticationService.forgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok().body("Password has been reset");
    }

}
