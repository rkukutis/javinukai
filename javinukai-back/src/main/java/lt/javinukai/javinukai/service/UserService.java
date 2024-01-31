package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User newUser) {
        User createdUser = userRepository.save(newUser);
        log.debug("{}: Created new user {}",this.getClass().getName(), createdUser.getUuid());
        return createdUser;
    }
    public User getUser(UUID userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User " + userId + " not found!"));
        log.debug("Fetched user {} from database", user.getUuid());
        return user;
    }

    public List<User> getUsers() {
        // this needs pagination
        List<User> users =  userRepository.findAll();
        log.debug("{}: Fetched all users from database", this.getClass().getName());
        return users;
    }
    public User updateUser(User updatedUser, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User " + userId + " not found!"));
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
    public List<User> deleteUser(UUID userId) {
        try {
        userRepository.deleteById(userId);
        log.debug("Deleted user {}", userId);
        } catch (IllegalArgumentException exception) {
            throw new UserNotFoundException(userId);
        }
        return userRepository.findAll();
    }
}
