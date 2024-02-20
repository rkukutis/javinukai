package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.dto.request.user.UserUpdateRequest;
import lt.javinukai.javinukai.entity.User;

public class UserMapper {

    private UserMapper(){}

    public static User mapToUser(UserRegistrationRequest userRegistrationRequest) {
        return User.builder()
                .name(userRegistrationRequest.getName())
                .surname(userRegistrationRequest.getSurname())
                .email(userRegistrationRequest.getEmail())
                .password(userRegistrationRequest.getPassword())
                .institution(userRegistrationRequest.getInstitution())
                .isFreelance(userRegistrationRequest.getInstitution() == null)
                .birthYear(userRegistrationRequest.getBirthYear())
                .isEnabled(true)
                .isNonLocked(true)
                .role(UserRole.USER)
                .maxSinglePhotos(50)
                .maxCollections(50)
                .phoneNumber(userRegistrationRequest.getPhoneNumber())
                .build();
    }

    public static User mapToUser(UserUpdateRequest updateDTO) {
        return User.builder()
                .name(updateDTO.getName())
                .surname(updateDTO.getSurname())
                .email(updateDTO.getEmail())
                .institution(updateDTO.getInstitution())
                .isFreelance(updateDTO.getInstitution() == null)
                .birthYear(updateDTO.getBirthYear())
                .maxSinglePhotos(updateDTO.getMaxSinglePhotos())
                .maxCollections(updateDTO.getMaxCollections())
                .phoneNumber(updateDTO.getPhoneNumber())
                .build();
    }
}
