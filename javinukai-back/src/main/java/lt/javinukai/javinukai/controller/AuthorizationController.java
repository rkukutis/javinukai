package lt.javinukai.javinukai.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.config.security.AuthenticationService;
import lt.javinukai.javinukai.dto.AuthenticationResponse;
import lt.javinukai.javinukai.dto.LoginDTO;
import lt.javinukai.javinukai.dto.RegistrationDTO;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthorizationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @Value("${app.constants.security.jwt-cookie-valid-hours}")
    private int jwtValidTimeHours;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegistrationDTO registration,
                                         HttpServletResponse response, HttpServletRequest request) throws URISyntaxException {
        AuthenticationResponse auth = authenticationService.register(registration);
        String cookieString = getResponseCookie("jwt", auth.getToken(), jwtValidTimeHours).toString();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieString);
        return ResponseEntity.ok().body(auth.getUser());
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDTO login, HttpServletResponse response) {
        AuthenticationResponse auth = authenticationService.login(login);
        String cookieString = getResponseCookie("jwt", auth.getToken(), jwtValidTimeHours).toString();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieString);
        return ResponseEntity.ok().body(auth.getUser());
    }

    @GetMapping("/test")
    public ResponseEntity<String> testCookie(@CookieValue("jwt") String jwt) {
        return ResponseEntity.ok().body("Received JWT from cookie: " + jwt);
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
}
