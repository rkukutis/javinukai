package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PhotoCollectionRepository photoCollectionRepository;

    public User createUser(UserRegistrationRequest registrationRequest) {
       return authenticationService.register(registrationRequest, true);
    }

    public User getUser(UUID userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
        log.debug("Fetched user {} from database", user.getId());
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
        log.info("{}: Updating user {}", this.getClass().getName(), user.getId());
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setEmail(updatedUser.getEmail());
        user.setBirthYear(updatedUser.getBirthYear());
        user.setIsFreelance(updatedUser.getIsFreelance());
        user.setInstitution(updatedUser.getInstitution());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        return userRepository.save(user);
    }

    public User updateUserForAdmin(UUID userId, User updateUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        UserRole previousRole = user.getRole();
        boolean previousIsNonLocked = user.getIsNonLocked();
        if (!previousRole.equals(updateUser.getRole())) {
            user.setRole(updateUser.getRole());
            log.info("Updating user {} role from {} to {}", user.getId(),
                    previousRole, updateUser.getRole());
        } else if (previousIsNonLocked != updateUser.getIsNonLocked()) {
            user.setIsNonLocked(updateUser.getIsNonLocked());
            log.info("Updating user {} isNonLocked state from {} to {}",
                    user.getId(), previousIsNonLocked, updateUser.getIsNonLocked());
        } else if (!user.getMaxTotal().equals(updateUser.getMaxTotal()) ||
                !user.getMaxSinglePhotos().equals(updateUser.getMaxSinglePhotos()) ||
                !user.getMaxCollections().equals(updateUser.getMaxCollections())
        ) {
            user.setMaxTotal(updateUser.getMaxTotal());
            user.setMaxSinglePhotos(updateUser.getMaxSinglePhotos());
            user.setMaxCollections(updateUser.getMaxCollections());
            user.setCustomLimits(true);
            log.info("Updating user {} photo upload limits", user.getId());
        }
        return userRepository.save(user);
    }

    public List<User> deleteUser(UUID userId) {
        if (userRepository.existsById(userId)) {
            List<PhotoCollection> collectionsToUpdate = photoCollectionRepository.findLikedCollectionsByJuryId(userId);
            userRepository.deleteById(userId);
            log.debug("Deleted user {}", userId);
            for (PhotoCollection collection: collectionsToUpdate) {
                collection.setLikesCount(collection.getJuryLikes().size());
            }
            photoCollectionRepository.saveAll(collectionsToUpdate);
            return userRepository.findAll();
        } else {
            throw new UserNotFoundException(userId);
        }
    }
}
