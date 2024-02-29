package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.ParticipationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.ParticipationRequestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final ContestRepository contestRepository;




//    @Autowired
//    public ParticipationRequestService(ParticipationRequestRepository participationRequestRepository) {
//        this.participationRequestRepository = participationRequestRepository;
//
//    }

    public List<ParticipationRequest> findAllRequests(){

        return participationRequestRepository.findAll();
    }

    public ParticipationRequest createParticipationRequest(Contest contest){

        ParticipationRequest request = participationRequestRepository.save(ParticipationRequest.builder()
                        .canParticipate(true)
                        .participationAccepted(false)
                        .contest(contest)
                        .build());
        return request;
    }

}
