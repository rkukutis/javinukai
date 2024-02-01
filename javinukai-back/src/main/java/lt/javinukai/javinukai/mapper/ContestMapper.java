package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;

public class ContestMapper {

    public static Contest contestDTOToContest(ContestDTO contestDTO) {
        return Contest.builder()
                .name(contestDTO.getName())
                .description(contestDTO.getDescription())
                .startDate(contestDTO.getStartDate())
                .endDate(contestDTO.getEndDate())
                .build();
    }

    public static ContestDTO contestToContestDTO(Contest contest) {
        return ContestDTO.builder()
                .name(contest.getName())
                .description(contest.getDescription())
                .startDate(contest.getStartDate())
                .endDate(contest.getEndDate())
                .build();
    }

}
