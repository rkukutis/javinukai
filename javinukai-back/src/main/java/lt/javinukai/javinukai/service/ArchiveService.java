package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.response.ArchivingResponse;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.PastCompetitionRecord;
import lt.javinukai.javinukai.repository.ArchiveRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ArchiveService {

    private final CompetitionRecordRepository competitionRecordRepository;
    private final ArchiveRepository archiveRepository;

    @Autowired
    public ArchiveService(ArchiveRepository archiveRepository,
                          CompetitionRecordRepository competitionRecordRepository) {
        this.archiveRepository = archiveRepository;
        this.competitionRecordRepository = competitionRecordRepository;
    }

    public ArchivingResponse endAndAddToArchive(UUID contestID) {

        if (competitionRecordRepository.existsById(contestID)) {

            final List<CompetitionRecord> competitionRecords = competitionRecordRepository.findByContestId(contestID);
            final List<PastCompetitionRecord> pastCompetitionRecords = new ArrayList<>();

            for (CompetitionRecord r : competitionRecords) {
                PastCompetitionRecord pastCompetition = PastCompetitionRecord.builder()
                        .firstName(r.getUser().getName())
                        .lastName(r.getUser().getSurname())
                        .email(r.getUser().getEmail())
                        .categoryName(r.getCategory().getName())
                        .categoryDescription(r.getCategory().getDescription())
                        .contestName(r.getContest().getName())
                        .contestDescription(r.getContest().getDescription())
                        .build();
                archiveRepository.save(pastCompetition);
                pastCompetitionRecords.add(pastCompetition);
            }
            log.info("{}: Contest was archived with ID: {}", this.getClass().getName(), contestID);

            return ArchivingResponse.builder()
                    .pastCompetitionRecords(pastCompetitionRecords)
                    .httpStatus(HttpStatus.CREATED)
                    .message("")
                    .build();
        } else {
            throw new EntityNotFoundException("Contest was not found with ID: " + contestID);
        }
    }
}
