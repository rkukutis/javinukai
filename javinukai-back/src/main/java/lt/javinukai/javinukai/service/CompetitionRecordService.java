package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CompetitionRecordDTO;
import lt.javinukai.javinukai.dto.response.UserParticipationResponse;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
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
@RequiredArgsConstructor
public class CompetitionRecordService {

    private final ContestRepository contestRepository;
    private final CompetitionRecordRepository competitionRecordRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserParticipationResponse createUsersCompetitionRecords(UUID contestID, UUID userID) {

        final Contest contestToParticipateIn = contestRepository.findById(contestID).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + contestID));

        final User participantUser = userRepository.findById(userID).orElseThrow(
                ()-> new UserNotFoundException(userID));

        final List<CompetitionRecord> participationInstances = competitionRecordRepository
                .findByUserAndContest(participantUser, contestToParticipateIn);

        HttpStatus httpStatus;
        String message;

        if (!participationInstances.isEmpty()) {
            log.info("{}: User already participates in the contest", this.getClass().getName());
            httpStatus = HttpStatus.OK;
            message = "Request for user participation completed, already a participant";

            return UserParticipationResponse.builder()
                    .records(participationInstances)
                    .message(message)
                    .httpStatus(httpStatus)
                    .build();
        }

        // čia matau sukuria record'us visoms kategorijoms, reikia tikėtis, kad kategorijų nebus daug
        final List<CompetitionRecord> usersCompetitionRecords = new ArrayList<>();
        for (Category category : contestToParticipateIn.getCategories()) {
            final CompetitionRecord record = CompetitionRecord.builder()
                    .contest(contestToParticipateIn)
                    .user(participantUser)
                    .category(category)
                    .maxPhotos(category.getTotalSubmissions()) //ateity reiks keisti į kintamąjį su logika
                    .build();
            final CompetitionRecord savedRecord = competitionRecordRepository.save(record);
            usersCompetitionRecords.add(savedRecord);
        }

        httpStatus = HttpStatus.CREATED;
        message = "Request for participation completed, records added to database";

        return UserParticipationResponse.builder()
                .records(usersCompetitionRecords)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }

    // this will retrieve the entries for the jury
    public Page<CompetitionRecord> retrieveCompetitionRecords(Pageable pageable, String keyword,
                                                                 UUID contestId, UUID categoryId) {
        if (keyword == null || keyword.isEmpty()) {
            log.info("{}: Retrieving all competition records from database", this.getClass().getName());
            return competitionRecordRepository.findByCategoryIdAndContestId(pageable, categoryId, contestId);
        } else {
            log.info("{}: Retrieving categories by use email", this.getClass().getName());
            final User userToFind = userRepository.findByEmail(keyword)
                    .orElseThrow(()-> new UserNotFoundException(keyword));
            return competitionRecordRepository.findByUser(userToFind, pageable);
        }
    }

    public Page<CompetitionRecord> retrieveAllUserCompetitionRecords(Pageable pageable, UUID userId) {
        return competitionRecordRepository.findByUserId(pageable, userId);
    }



    @Transactional
    public CompetitionRecord updateCompetitionRecord(UUID recordID, CompetitionRecordDTO competitionRecordDTO) {

        CompetitionRecord competitionRecordToUpdate = competitionRecordRepository.findById(recordID).orElseThrow(
                () -> new EntityNotFoundException("Competition record was not found with ID: " + recordID));

        competitionRecordToUpdate.setMaxPhotos(competitionRecordDTO.getMaxPhotos());
        return competitionRecordRepository.save(competitionRecordToUpdate);
    }

    @Transactional
    public void deleteRecord(UUID recordID) {
        if (competitionRecordRepository.existsById(recordID)) {
            competitionRecordRepository.deleteById(recordID);
            log.info("{}: Deleted record from the database with ID: {}", this.getClass().getName(), recordID);
        } else {
            throw new EntityNotFoundException("Record was not found with ID: " + recordID);
        }
    }

    public CompetitionRecord retrieveUserCompetitionRecord(UUID categoryId, UUID contestId, UUID userId) {
        return competitionRecordRepository.findByCategoryIdAndContestIdAndUserId(categoryId, contestId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Competition record for user %s in contest %s category %s not found",
                        userId, contestId, categoryId
                )));
    }
}
