package lt.javinukai.javinukai.mapper;

import lt.javinukai.javinukai.dto.request.contest.CompetitionRecordDTO;
import lt.javinukai.javinukai.entity.CompetitionRecord;

public class CompetitionRecordMapper {

    public static CompetitionRecord recordDTOToRecord(CompetitionRecordDTO competitionRecordDTO) {
        return CompetitionRecord.builder()
                .maxPhotos(competitionRecordDTO.getMaxPhotos())
                .photos(competitionRecordDTO.getPhotos())
                .build();
    }



}
