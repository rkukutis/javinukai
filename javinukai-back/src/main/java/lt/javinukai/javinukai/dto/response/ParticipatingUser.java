package lt.javinukai.javinukai.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipatingUser {
    private String firstName;
    private String lastName;
    private String email;
}
