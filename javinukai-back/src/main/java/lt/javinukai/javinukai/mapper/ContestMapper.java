package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;

public class ContestMapper {

    public static Contest contestDTOToContest(ContestDTO contestDTO) {
        return Contest.builder()
                .name(contestDTO.getName())
                .description(contestDTO.getDescription())
                .maxTotalSubmissions(contestDTO.getMaxTotalSubmissions())
                .maxUserSubmissions(contestDTO.getMaxUserSubmissions())
                .startDate(contestDTO.getStartDate())
                .endDate(contestDTO.getEndDate())
                .build();
    }
}
