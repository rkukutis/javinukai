package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.request.user.UserUpdateRequest;
import lt.javinukai.javinukai.entity.User;

public class UserMapper {

    private UserMapper(){}

    public static User mapToUser(UserUpdateRequest updateDTO) {
        return User.builder()
                .name(updateDTO.getName())
                .surname(updateDTO.getSurname())
                .email(updateDTO.getEmail())
                .institution(updateDTO.getInstitution())
                .isFreelance(updateDTO.getInstitution() == null)
                .birthYear(updateDTO.getBirthYear())
                .maxTotal(updateDTO.getMaxTotal())
                .maxSinglePhotos(updateDTO.getMaxSinglePhotos())
                .maxCollections(updateDTO.getMaxCollections())
                .phoneNumber(updateDTO.getPhoneNumber())
                .build();
    }
}
