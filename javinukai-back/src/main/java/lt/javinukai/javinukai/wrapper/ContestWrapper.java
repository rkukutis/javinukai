package lt.javinukai.javinukai.wrapper;

import lombok.Builder;
import lombok.Getter;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.enums.ParticipationStatus;

@Builder
@Getter
public class ContestWrapper {

    private Contest contest;
    private ParticipationStatus status;
}
