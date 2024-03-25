package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.request.user.UserUpdateRequest;
import lt.javinukai.javinukai.dto.response.ParticipatingUser;
import lt.javinukai.javinukai.entity.User;

public class UserMapper {

    private UserMapper(){}

    public static User mapToUser(UserUpdateRequest updateDTO) {
        return User.builder()
                .name(updateDTO.getName())
                .surname(updateDTO.getSurname())
                .email(updateDTO.getEmail())
                .institution(updateDTO.getInstitution())
                .isFreelance(updateDTO.getInstitution() == null || updateDTO.getInstitution().isBlank())
                .birthYear(updateDTO.getBirthYear())
                .maxTotal(updateDTO.getMaxTotal())
                .maxSinglePhotos(updateDTO.getMaxSinglePhotos())
                .maxCollections(updateDTO.getMaxCollections())
                .phoneNumber(updateDTO.getPhoneNumber())
                .build();
    }

        public static ParticipatingUser userToParticipatingUsr(User user) {
        return ParticipatingUser.builder()
                .firstName(user.getName())
                .lastName(user.getSurname())
                .email(user.getEmail())
                .build();
    }
}
