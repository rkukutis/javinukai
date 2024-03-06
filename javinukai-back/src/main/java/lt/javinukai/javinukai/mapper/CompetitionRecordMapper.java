package lt.javinukai.javinukai.mapper;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CompetitionRecordDTO;
import lt.javinukai.javinukai.dto.response.CompetitionRecordResponse;
import lt.javinukai.javinukai.entity.CompetitionRecord;

@Slf4j
public class CompetitionRecordMapper {

    public static CompetitionRecord recordDTOToRecord(CompetitionRecordDTO competitionRecordDTO) {
        return CompetitionRecord.builder()
                .maxPhotos(competitionRecordDTO.getMaxPhotos())
                .build();
    }

    public static CompetitionRecordResponse recordToRecordResponse(CompetitionRecord record) {
        return CompetitionRecordResponse.builder()
                .usersFirstName(record.getUser().getName())
                .usersLastName(record.getUser().getSurname())
                .usersEmailAddress(record.getUser().getEmail())
                .contestName(record.getContest().getName())
                .contestDescription(record.getContest().getDescription())
                .contestStartDate(record.getContest().getStartDate())
                .contestEndDate(record.getContest().getEndDate())
                .categoryName(record.getCategory().getName())
                .categoryDescription(record.getCategory().getDescription())
                .totalSubmissions(record.getCategory().getTotalSubmissions())
                .build();
    }
}
