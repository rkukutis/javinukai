package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.ParticipationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.ParticipationRequestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
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

    public List<ParticipationRequest> findAllRequests() {

        return participationRequestRepository.findAll();
    }

    public ParticipationRequest createParticipationRequest(UUID contestId, UUID userId) {
        Contest temp = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException("Contest was not found with ID: " + contestId));
        User user = userRepository.findById(userId).get();

        ParticipationRequest request = participationRequestRepository.save(ParticipationRequest.builder()
                .canParticipate(null)
                .contest(temp)
                .user(user)
                .build());
        return request;
    }

    public ParticipationRequest getRequestById(UUID requestId) {
        ParticipationRequest participationRequest =  participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Participation request was not found with ID: " + requestId));
        log.debug("Fetched participation request {} from database", participationRequest.getRequestId());
        return participationRequest;
    }

    @Transactional
    public ParticipationRequest updateRequestStatus (UUID requestId, Boolean canParticipate){
        final ParticipationRequest tempRequest = participationRequestRepository.findById(requestId)
                .orElseThrow(()->new EntityNotFoundException("Participation request was not found with ID: " + requestId));
        tempRequest.setCanParticipate(canParticipate);
        return participationRequestRepository.save(tempRequest);

    }


    @Transactional
    public List<ParticipationRequest> deleteParticipationRequest(UUID participationRequestId) {
        if (participationRequestRepository.existsById(participationRequestId)) {
            participationRequestRepository.deleteById(participationRequestId);
            log.debug("Deleted participation request {}", participationRequestId);
            return participationRequestRepository.findAll();
        } else {
            throw new EntityNotFoundException("Participation request was not found with ID: " + participationRequestId);
        }
    }
}
