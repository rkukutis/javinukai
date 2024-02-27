package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CompetitionRecordDTO;
import lt.javinukai.javinukai.dto.response.CategoryCreationResponse;
import lt.javinukai.javinukai.dto.response.CompetitionRecordResponse;
import lt.javinukai.javinukai.dto.response.UserParticipationResponse;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.mapper.CompetitionRecordMapper;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

//    @Transactional
//    public List<CompetitionRecordResponse> createUsersCompetitionRecords(UUID contestID, UUID userID) {
//
//        final Contest contestToParticipateIn = contestRepository.findById(contestID).orElseThrow(
//                () -> new EntityNotFoundException("Contest was not found with ID: " + contestID));
//
//        final User participantUser = userRepository.findById(userID).orElseThrow(
//                ()-> new UserNotFoundException(userID));
//
//        final List<CompetitionRecord> usersCompetitionRecords = new ArrayList<>();
//
//        for (Category category : contestToParticipateIn.getCategories()) {
//
//            final CompetitionRecord record = CompetitionRecord.builder()
//                    .contest(contestToParticipateIn)
//                    .user(participantUser)
//                    .category(category)
//                    .maxPhotos(category.getTotalSubmissions()) //ateity reiks keisti į kintamąjį su logika
//                    .build();
//            final CompetitionRecord savedRecord = competitionRecordRepository.save(record);
//            usersCompetitionRecords.add(savedRecord);
//        }
//
//        return usersCompetitionRecords.stream()
//                .map(CompetitionRecordMapper::recordToRecordResponse)
//                .collect(Collectors.toList());
//    }

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

//    public Page<CompetitionRecordResponse> retrieveAllCompetitionRecords(Pageable pageable, String keyword) {
//
//        if (keyword == null || keyword.isEmpty()) {
//            log.info("{}: Retrieving all competition records from database", this.getClass().getName());
//            final Page<CompetitionRecord> page = competitionRecordRepository.findAll(pageable);
//            final List<CompetitionRecordResponse> records = page.getContent()
//                    .stream()
//                    .map(CompetitionRecordMapper::recordToRecordResponse)
//                    .toList();
//            return new PageImpl<>(records);
//        } else {
//            log.info("{}: Retrieving categories by use email", this.getClass().getName());
//            final User userToFind = userRepository.findByEmail(keyword)
//                    .orElseThrow(()-> new UserNotFoundException(keyword));
//            final Page<CompetitionRecord> page = competitionRecordRepository.findByUser(userToFind, pageable);
//            final List<CompetitionRecordResponse> records = page.getContent()
//                    .stream()
//                    .map(CompetitionRecordMapper::recordToRecordResponse)
//                    .toList();
//            return new PageImpl<>(records);
//        }
//    }

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
