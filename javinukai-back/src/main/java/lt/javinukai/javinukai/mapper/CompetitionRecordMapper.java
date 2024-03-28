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
}
