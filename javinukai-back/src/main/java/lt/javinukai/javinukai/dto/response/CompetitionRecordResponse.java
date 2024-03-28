package lt.javinukai.javinukai.dto.response;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetitionRecordResponse {

    private String usersFirstName;
    private String usersLastName;
    private String usersEmailAddress;

    private String contestName;
    private String contestDescription;
    private ZonedDateTime contestStartDate;
    private ZonedDateTime contestEndDate;

    private String categoryName;
    private String categoryDescription;
    private long totalSubmissions;
}
