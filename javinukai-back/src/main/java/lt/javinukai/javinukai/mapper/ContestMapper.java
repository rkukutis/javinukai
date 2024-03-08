package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;

public class ContestMapper {

    public static Contest contestDTOToContest(ContestDTO contestDTO) {
        return Contest.builder()
                .name(contestDTO.getName())
                .description(contestDTO.getDescription())
//                .categories(contestDTO.getCategories())
                .totalSubmissions(contestDTO.getTotalSubmissions())
                .startDate(contestDTO.getStartDate())
                .endDate(contestDTO.getEndDate())
                .build();
    }

    public static ContestDTO contestToContestDTO(Contest contest) {
        return ContestDTO.builder()
                .id(contest.getId())
                .name(contest.getName())
//                .categories(contest.getCategories())
                .description(contest.getDescription())
                .totalSubmissions(contest.getTotalSubmissions())
                .startDate(contest.getStartDate())
                .endDate(contest.getEndDate())
                .build();
    }

    public static ContestDTO contestToContestDTOTest(Contest contest) {
        return ContestDTO.builder()
                .id(contest.getId())
                .name(contest.getName())
                .categories(contest.getCategories())
                .description(contest.getDescription())
                .totalSubmissions(contest.getTotalSubmissions())
                .startDate(contest.getStartDate())
                .endDate(contest.getEndDate())
                .build();
    }





}
