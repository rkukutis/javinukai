package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.exception.UserAlreadyExistsException;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.constants.user-defaults.max-photos.single}")
    private int defaultMaxSinglePhotos;

    @Value("${app.constants.user-defaults.max-photos.collection}")
    private int defaultMaxCollections;


    public User createUser(User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            log.warn("Can not create new user with email {} as one already exists", newUser.getEmail());
            throw new UserAlreadyExistsException("User with email " + newUser.getEmail() + " already exists!");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setMaxSinglePhotos(defaultMaxSinglePhotos);
        newUser.setMaxCollections(defaultMaxCollections);
        User createdUser = userRepository.save(newUser);
        log.info("Created new user: {}", createdUser);
        return createdUser;
    }
    public User getUser(UUID userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
        log.debug("Fetched user {} from database", user.getUuid());
        return user;
    }

    public Page<User> getUsers(PageRequest pageRequest, String surname) {
        if (surname == null) {
            log.info("Fetching all users from database");
            return userRepository.findAll(pageRequest);
        } else {
            log.info("Fetching all users with surname {} from database", surname);
            return userRepository.findBySurnameContainingIgnoreCase(surname, pageRequest);
        }
    }
    public User updateUser(User updatedUser, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.debug("{}: Updating user {}", this.getClass().getName(), user.getUuid());
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setEmail(updatedUser.getEmail());
        user.setBirthYear(updatedUser.getBirthYear());
        user.setIsFreelance(updatedUser.getIsFreelance());
        user.setInstitution(updatedUser.getInstitution());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setMaxSinglePhotos(updatedUser.getMaxSinglePhotos());
        user.setMaxCollections(updatedUser.getMaxCollections());
        return userRepository.save(user);
    }

    public User updateUserForAdmin(UUID userId, User updateUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        UserRole previousRole = user.getRole();
        boolean previousIsNonLocked = user.getIsNonLocked();
        if (!previousRole.equals(updateUser.getRole())) {
            user.setRole(updateUser.getRole());
            log.info("Updating user {} role from {} to {}", user.getUuid(),
                    previousRole, updateUser.getRole());
        } else if (previousIsNonLocked != updateUser.getIsNonLocked()) {
            user.setIsNonLocked(updateUser.getIsNonLocked());
            log.info("Updating user {} isNonLocked state from {} to {}",
                    user.getUuid(), previousIsNonLocked, updateUser.getIsNonLocked());
        }
        return userRepository.save(user);
    }


    public List<User> deleteUser(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            log.debug("Deleted user {}", userId);
            return userRepository.findAll();
        } else {
            throw new UserNotFoundException(userId);
        }
    }
}
