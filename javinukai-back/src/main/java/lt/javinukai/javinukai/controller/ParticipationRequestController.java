package lt.javinukai.javinukai.controller;

import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.ParticipationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.service.ParticipationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/test")
public class ParticipationRequestController {
    private ParticipationRequestService participationRequestService;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService participationRequestService) {
        this.participationRequestService = participationRequestService;
    }

//    @GetMapping("/request")
//    public List<ParticipationRequest> findAllRequests(){
//        return participationRequestService.findAllRequests();
//    }

    @PostMapping("/request")
    public ParticipationRequest createParticipationRequest(@AuthenticationPrincipal User user,
                                                           @RequestParam UUID contestId) {
        ParticipationRequest newRequest = participationRequestService.createParticipationRequest(contestId, user.getUuid());
        return newRequest;
    }


}
