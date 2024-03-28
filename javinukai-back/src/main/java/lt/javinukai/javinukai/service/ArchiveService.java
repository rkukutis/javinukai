package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.response.ArchivingResponse;
import lt.javinukai.javinukai.entity.*;
import lt.javinukai.javinukai.repository.ArchivedContestRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ArchiveService {

    private final ContestRepository contestRepository;
    private final CompetitionRecordRepository competitionRecordRepository;
    private final ArchivedContestRepository archivedContestRepository;
    private final PhotoCollectionRepository photoCollectionRepository;

    @Autowired
    public ArchiveService(CompetitionRecordRepository competitionRecordRepository,
                          ContestRepository contestRepository,
                          ArchivedContestRepository archivedContestRepository,
                          PhotoCollectionRepository photoCollectionRepository) {
        this.competitionRecordRepository = competitionRecordRepository;
        this.contestRepository = contestRepository;
        this.archivedContestRepository = archivedContestRepository;
        this.photoCollectionRepository = photoCollectionRepository;
    }

    @Transactional
    public ArchivingResponse addToArchive(UUID contestID, List<String> winners) {

        if (contestRepository.existsById(contestID)) {

            final Contest contestToArchive = contestRepository.findById(contestID).orElseThrow(
                    () -> new EntityNotFoundException("Contest was not found with ID: " + contestID));

            final PastCompetition pastContest = PastCompetition.builder()
                    .contestID(contestID)
                    .contestName(contestToArchive.getName())
                    .contestDescription(contestToArchive.getDescription())
                    .categories(null)
                    .startDate(contestToArchive.getStartDate())
                    .endDate(contestToArchive.getEndDate())
                    .participants(null)
                    .winners(winners)
                    .build();

            final List<Category> contestCategories = contestToArchive.getCategories();
            final List<String> categoriesToStore = new ArrayList<>();
            for (Category c : contestCategories) {
                categoriesToStore.add(c.getName());
            }
            pastContest.setCategories(categoriesToStore);

            final List<CompetitionRecord> competitionRecords = competitionRecordRepository.findByContestId(contestID);
            final List<String> participantsToStore = new ArrayList<>();
            for (CompetitionRecord cr : competitionRecords) {
                String userCSV = String.format("%s,%s,%s",
                        cr.getUser().getName(),
                        cr.getUser().getSurname(),
                        cr.getUser().getEmail());
                    photoCollectionRepository.deleteByCompetitionRecordId(cr.getId());
                if (!participantsToStore.contains(userCSV)) {
                    participantsToStore.add(userCSV);
                }
            }
            pastContest.setParticipants(participantsToStore);

            archivedContestRepository.save(pastContest);
            log.info("{}: Contest was archived with ID: {}", this.getClass().getName(), contestID);

            if (!contestToArchive.isArchived()) {
                contestToArchive.setArchived(true);
            }

            for (Category c : contestToArchive.getCategories()) {
                c.setUploadedPhotos(null);
            }

            return ArchivingResponse.builder()
                    .pastCompetition(pastContest)
                    .httpStatus(HttpStatus.CREATED)
                    .message("Request for archiving a contest")
                    .build();
        } else {
            throw new EntityNotFoundException("Contest was not found with ID: " + contestID);
        }
    }

    public Page<PastCompetition> retrieveAllRecords(Pageable pageable, String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            log.info("{}: Retrieving all past competition record list from database", this.getClass());
            return archivedContestRepository.findAll(pageable);
        } else {
            log.info("{}: Retrieving past competition records by name", this.getClass().getName());
            return archivedContestRepository.findByContestNameContainingIgnoreCase(keyword, pageable);
        }
    }
}
