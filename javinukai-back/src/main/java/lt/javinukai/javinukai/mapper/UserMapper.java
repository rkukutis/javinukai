package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.RegistrationDTO;
import lt.javinukai.javinukai.dto.UserUpdateDTO;
import lt.javinukai.javinukai.entity.User;

public class UserMapper {

    private UserMapper(){}

    public static User mapToUser(RegistrationDTO registrationDTO) {
        return User.builder()
                .name(registrationDTO.getName())
                .surname(registrationDTO.getSurname())
                .email(registrationDTO.getEmail())
                .institution(registrationDTO.getInstitution())
                .isFreelance(registrationDTO.getInstitution() == null)
                .birthYear(registrationDTO.getBirthYear())
                .maxSinglePhotos(50)
                .maxCollections(50)
                .phoneNumber(registrationDTO.getPhoneNumber())
                .build();
    }

    public static User mapToUser(UserUpdateDTO updateDTO) {
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
