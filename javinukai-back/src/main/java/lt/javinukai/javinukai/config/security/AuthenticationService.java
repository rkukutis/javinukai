package lt.javinukai.javinukai.config.security;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.dto.AuthenticationResponse;
import lt.javinukai.javinukai.dto.LoginDTO;
import lt.javinukai.javinukai.dto.RegistrationDTO;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.UserRepository;
import lt.javinukai.javinukai.service.EmailService;
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
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.constants.user-defaults.max-photos.single}")
    private int defaultMaxSinglePhotos;

    @Value("${app.constants.user-defaults.max-photos.collection}")
    private int defaultMaxCollections;

    public AuthenticationResponse register(RegistrationDTO registrationDTO) {
        var user = User.builder()
                .name(registrationDTO.getName())
                .surname(registrationDTO.getSurname())
                .email(registrationDTO.getEmail())
                .phoneNumber(registrationDTO.getPhoneNumber())
                .birthYear(registrationDTO.getBirthYear())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(UserRole.USER)
                .maxSinglePhotos(defaultMaxSinglePhotos)
                .maxCollections(defaultMaxCollections)
                .isNonLocked(true)
                .isEnabled(true)
                .institution(registrationDTO.getInstitution())
                .isFreelance(registrationDTO.getInstitution() == null)
                .build();
        User createdUser = userRepository.save(user);
        emailService.sendEmailConfirmation(createdUser);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(createdUser)
                .build();

    }

    public AuthenticationResponse login(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
                        loginDTO.getPassword()));
        var user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Wrong Credentials"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

}
