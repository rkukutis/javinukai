package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.User;
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
        log.debug("Creating new user {}", createdUser.getUuid());
        return createdUser;
    }
    public User getUser(UUID userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User " + userId + " not found!"));
        log.debug("Fetching user {} from database", user.getUuid());
        return user;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public User updateUser(User updatedUser, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User " + userId + " not found!"));
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setEmail(updatedUser.getEmail());
        user.setBirthYear(updatedUser.getBirthYear());
        user.setIsFreelance(updatedUser.getIsFreelance());
        user.setInstitution(updatedUser.getInstitution());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        return userRepository.save(user);
    }
    public List<User> deleteUser(UUID userId) {
        userRepository.deleteById(userId);
        return userRepository.findAll();
    }
}
