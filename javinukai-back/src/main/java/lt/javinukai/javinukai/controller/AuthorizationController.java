package lt.javinukai.javinukai.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.config.security.AuthenticationService;
import lt.javinukai.javinukai.dto.AuthenticationResponse;
import lt.javinukai.javinukai.dto.LoginDTO;
import lt.javinukai.javinukai.dto.RegistrationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthorizationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegistrationDTO registration) {
        return ResponseEntity.ok().body(authenticationService.register(registration));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO login) {
        return ResponseEntity.ok().body(authenticationService.login(login));
    }

}
