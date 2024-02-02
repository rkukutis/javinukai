package lt.javinukai.javinukai.config.security;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.dto.response.AuthenticationResponse;
import lt.javinukai.javinukai.dto.request.auth.LoginRequest;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.constants.user-defaults.max-photos.single}")
    private int defaultMaxSinglePhotos;

    @Value("${app.constants.user-defaults.max-photos.collection}")
    private int defaultMaxCollections;

    public AuthenticationResponse register(UserRegistrationRequest userRegistrationRequest) {
        var user = User.builder()
                .name(userRegistrationRequest.getName())
                .surname(userRegistrationRequest.getSurname())
                .email(userRegistrationRequest.getEmail())
                .phoneNumber(userRegistrationRequest.getPhoneNumber())
                .birthYear(userRegistrationRequest.getBirthYear())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .role(UserRole.USER)
                .maxSinglePhotos(defaultMaxSinglePhotos)
                .maxCollections(defaultMaxCollections)
                .isNonLocked(true)
                .isEnabled(true)
                .institution(userRegistrationRequest.getInstitution())
                .isFreelance(userRegistrationRequest.getInstitution() == null)
                .build();
        User createdUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(createdUser)
                .build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Wrong Credentials"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }
}
