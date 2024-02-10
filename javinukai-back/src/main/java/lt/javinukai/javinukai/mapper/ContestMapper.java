package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;

public class ContestMapper {

    public static Contest contestDTOToContest(ContestDTO contestDTO) {
        return Contest.builder()
                .contestName(contestDTO.getContestName())
                .description(contestDTO.getDescription())
                .totalSubmissions(contestDTO.getTotalSubmissions())
                .startDate(contestDTO.getStartDate())
                .endDate(contestDTO.getEndDate())
                .build();
    }



}
