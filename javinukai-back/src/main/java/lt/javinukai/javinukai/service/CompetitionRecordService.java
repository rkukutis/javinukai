package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CompetitionRecordDTO;
import lt.javinukai.javinukai.dto.response.ParticipatingUser;
import lt.javinukai.javinukai.dto.response.UserParticipationResponse;
import lt.javinukai.javinukai.entity.*;
import lt.javinukai.javinukai.enums.PhotoSubmissionType;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.mapper.CompetitionRecordMapper;
import lt.javinukai.javinukai.mapper.UserMapper;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final PhotoCollectionRepository photoCollectionRepository;

    @Value("${app.constants.user-defaults.max-photos.single}")
    private int defaultMaxSinglePhotos;
    @Value("${app.constants.user-defaults.max-photos.collection}")
    private int defaultMaxCollections;

    private int getLimit(User approvedUser, Category newCategory) {
        int limit = 0;
        if (approvedUser.isCustomLimits()) {
            limit = newCategory.getType() == PhotoSubmissionType.SINGLE ?
                    approvedUser.getMaxSinglePhotos() :
                    approvedUser.getMaxCollections();
        } else {
            limit = newCategory.getType() == PhotoSubmissionType.SINGLE ?
                    defaultMaxSinglePhotos :
                    defaultMaxCollections;
        }
        return limit;
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
            int limit = getLimit(participantUser, category);
            final CompetitionRecord record = CompetitionRecord.builder()
                    .contest(contestToParticipateIn)
                    .user(participantUser)
                    .category(category)
                    .maxPhotos(limit)
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

    public void createRecordsForApprovedUsers(List<Category> newCategories, UUID contestId) {
        // find all approved users
       List<User> approvedUsers = competitionRecordRepository.findByContestId(contestId)
               .stream()
               .map((CompetitionRecord::getUser))
               .distinct()
               .toList();

       Contest contest = contestRepository.findById(contestId).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + contestId));

        for (User approvedUser : approvedUsers) {
            for (Category newCategory : newCategories) {
                int limit = getLimit(approvedUser, newCategory);
                CompetitionRecord record = CompetitionRecord.builder()
                        .contest(contest)
                        .user(approvedUser)
                        .category(newCategory)
                        .maxPhotos(limit)
                        .build();

                competitionRecordRepository.save(record);
            }
        }
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

    public Page<ParticipatingUser> retrieveCompetingParticipants(Pageable pageable, UUID contestId, String keyword) {

        final List<CompetitionRecord> competitionRecords = competitionRecordRepository.findByContestId(contestId);
        final List<PhotoCollection> likedPhotos = new ArrayList<>();
        for (CompetitionRecord r : competitionRecords) {
            final UUID recordID = r.getId();
            List<PhotoCollection> rPhotoCollection =
                    photoCollectionRepository.findCollectionsByCompetitionRecordId(recordID);
            for (PhotoCollection p : rPhotoCollection) {
                if (p != null && !p.getJuryLikes().isEmpty()) {
                    likedPhotos.add(p);
                }
            }
        }

        log.info("{}: Retrieving still competing participants", this.getClass().getName());

        List<ParticipatingUser> authors = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        for (PhotoCollection pc : likedPhotos) {
            String email = pc.getAuthor().getEmail();
            final ParticipatingUser participant = UserMapper.userToParticipatingUsr(pc.getAuthor());
            if (!emails.contains(email)) {
                emails.add(email);
                authors.add(participant);
            }
        }
        return new PageImpl<>(authors);
    }

    @Transactional
    public CompetitionRecord updateCompetitionRecord(UUID recordID, CompetitionRecordDTO competitionRecordDTO) {
        final CompetitionRecord record = CompetitionRecordMapper.recordDTOToRecord(competitionRecordDTO);
        CompetitionRecord competitionRecordToUpdate = competitionRecordRepository.findById(recordID).orElseThrow(
                () -> new EntityNotFoundException("Competition record was not found with ID: " + recordID));
        competitionRecordToUpdate.setMaxPhotos(record.getMaxPhotos());
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

    public void deleteRecordsForCategories(List<Category> removedCategories, UUID contestId) {
        for (Category removedCategory : removedCategories) {
            competitionRecordRepository.deleteByCategoryIdAndContestId(removedCategory.getId(), contestId);
        }
    }
}
