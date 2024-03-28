package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.ParticipationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ParticipationRequestStatus;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.ParticipationRequestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final ContestRepository contestRepository;
    private final CompetitionRecordService competitionRecordService;
    private final EmailService emailService;

    public Page<ParticipationRequest> getAllRequests(PageRequest pageRequest, String contains) {
        if (contains == null) {
            return participationRequestRepository.findAll(pageRequest);
        } else {
            return participationRequestRepository.findByUserSurnameContainingIgnoreCase(pageRequest, contains);
        }
    }

    @Transactional
    public ParticipationRequest createParticipationRequest(UUID contestId, UUID userId) {
        List<ParticipationRequest> checkList = participationRequestRepository.findByUserIdAndContestId(userId, contestId);

        if (checkList.isEmpty()) {
            Contest tempContest = contestRepository.findById(contestId)
                    .orElseThrow(() -> new EntityNotFoundException("Contest was not found with ID: " + contestId));
            User tempUser = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User was not found with Id: " + userId));

            ParticipationRequest request = participationRequestRepository.save(ParticipationRequest.builder()
                    .requestStatus(ParticipationRequestStatus.PENDING)
                    .contest(tempContest)
                    .user(tempUser)
                    .build());
            return request;
        }
        return checkList.get(0);
    }

    public List<ParticipationRequest> getRequestByUserIdAndContestId(UUID userId, UUID contestId) {
        return participationRequestRepository.findByUserIdAndContestId(userId, contestId);
    }

    public ParticipationRequest getRequestById(UUID requestId) {
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Participation request was not found with ID: " + requestId));
        log.debug("Fetched participation request {} from database", participationRequest.getRequestId());
        return participationRequest;
    }

    @Transactional
    public ParticipationRequest updateRequestStatus(UUID requestId, ParticipationRequestStatus status) {
        final ParticipationRequest tempRequest = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Participation request was not found with ID: " + requestId));
        ParticipationRequestStatus currentStatus = tempRequest.getRequestStatus();
        if (currentStatus == ParticipationRequestStatus.ACCEPTED) {
            return participationRequestRepository.save(tempRequest);
        }
        tempRequest.setRequestStatus(status);
        if (status == ParticipationRequestStatus.ACCEPTED) {
            UUID userId = tempRequest.getUser().getId();
            UUID contestId = tempRequest.getContest().getId();
            competitionRecordService.createUsersCompetitionRecords(contestId, userId);
        }
        emailService.participationStatusChangeNotification(tempRequest.getUser(), tempRequest);
        return participationRequestRepository.save(tempRequest);
    }

    @Transactional
    public void deleteParticipationRequest(UUID participationRequestId) {
        if (participationRequestRepository.existsById(participationRequestId)) {
            participationRequestRepository.deleteById(participationRequestId);
            log.debug("Deleted participation request {}", participationRequestId);
        } else {
            throw new EntityNotFoundException("Participation request was not found with ID: " + participationRequestId);
        }
    }

    @Transactional
    public void deleteAllRequestsByContestId(UUID contestId) {
        Contest c = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException("not found"));
        participationRequestRepository.deleteParticipationRequestsByContest(c);
    }
}
