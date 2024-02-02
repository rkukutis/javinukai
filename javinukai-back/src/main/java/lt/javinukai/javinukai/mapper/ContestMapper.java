package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;

public class ContestMapper {

    public static Contest contestDTOToContest(ContestDTO contestDTO) {
        return Contest.builder()
                .name(contestDTO.getName())
                .description(contestDTO.getDescription())
                .category(contestDTO.getCategory())
                .totalSubmissions(contestDTO.getTotalSubmissions())
                .startDate(contestDTO.getStartDate())
                .endDate(contestDTO.getEndDate())
                .build();
    }



}
