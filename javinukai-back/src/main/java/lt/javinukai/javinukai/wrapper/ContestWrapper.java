package lt.javinukai.javinukai.wrapper;

import lombok.Builder;
import lombok.Getter;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.enums.ParticipationRequestStatus;

@Builder
@Getter
public class ContestWrapper {

    private Contest contest;
    private ParticipationRequestStatus status;
    private long totalEntries;
    private long maxUserEntries;
    private long userEntries;
}
