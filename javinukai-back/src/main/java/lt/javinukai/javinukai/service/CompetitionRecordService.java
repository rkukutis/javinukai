package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CompetitionRecordDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CompetitionRecordService {

    private final ContestRepository contestRepository;
    private final CompetitionRecordRepository competitionRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public CompetitionRecordService (ContestRepository contestRepository,
                                     CompetitionRecordRepository competitionRecordRepository,
                                     UserRepository userRepository) {
        this.contestRepository = contestRepository;
        this.competitionRecordRepository = competitionRecordRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<CompetitionRecord> createUsersCompetitionRecords(UUID contestID, UUID userID) {

        final Contest contestToParticipateIn = contestRepository.findById(contestID).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + contestID));

        final User participantUser = userRepository.findById(userID).orElseThrow(
                ()-> new UserNotFoundException(userID));

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

        return usersCompetitionRecords;
    }

    public Page<CompetitionRecord> retrieveAllCompetitionRecords(Pageable pageable, String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            log.info("{}: Retrieving all competition records from database", this.getClass().getName());
            return competitionRecordRepository.findAll(pageable);
        } else {
            log.info("{}: Retrieving categories by use email", this.getClass().getName());
            final User userToFind = userRepository.findByEmail(keyword)
                    .orElseThrow(()-> new UserNotFoundException(keyword));
            return competitionRecordRepository.findByUser(userToFind, pageable);
        }
    }

    @Transactional
    public CompetitionRecord updateCompetitionRecord(UUID recordID, CompetitionRecordDTO competitionRecordDTO) {

        CompetitionRecord competitionRecordToUpdate = competitionRecordRepository.findById(recordID).orElseThrow(
                () -> new EntityNotFoundException("Competition record was not found with ID: " + recordID));

        competitionRecordToUpdate.setMaxPhotos(competitionRecordDTO.getMaxPhotos());
        competitionRecordToUpdate.setPhotos(competitionRecordDTO.getPhotos());
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

}
