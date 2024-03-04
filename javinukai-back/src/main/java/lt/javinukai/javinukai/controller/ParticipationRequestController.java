package lt.javinukai.javinukai.controller;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.ParticipationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.service.ParticipationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ParticipationRequestController {
    private ParticipationRequestService participationRequestService;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService participationRequestService) {
        this.participationRequestService = participationRequestService;
    }

    @GetMapping("/requests")
    public List<ParticipationRequest> findAllRequests() {
        return participationRequestService.findAllRequests();
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<ParticipationRequest> getRequestById(@PathVariable UUID requestId) {
        log.info("Data for participation request {} requested", requestId);
        return ResponseEntity.ok().body(participationRequestService.getRequestById(requestId));
    }

    @PostMapping("/request")
    public ParticipationRequest createParticipationRequest(@AuthenticationPrincipal User user,
                                                           @RequestParam UUID contestId) {
        ParticipationRequest newRequest = participationRequestService.createParticipationRequest(contestId, user.getId());
        return newRequest;
    }

    @PatchMapping("/request/{requestId}")
    public ResponseEntity<ParticipationRequest> updateRequestStatus(@PathVariable UUID requestId, @RequestParam Boolean canParticipate) {
        return ResponseEntity.ok().body(participationRequestService.updateRequestStatus(requestId, canParticipate));
    }

//    @DeleteMapping("/request/{id}")
//    public ResponseEntity<List<ParticipationRequest>> deleteParticipationRequest(@PathVariable UUID id) {
//        return ResponseEntity.ok().body(participationRequestService.deleteParticipationRequest(id));
//    }

    @DeleteMapping(path = "/request/{requestId}")
    public ResponseEntity<?> deleteContest(@PathVariable @NotNull UUID requestId) {
        log.info("Request for deleting participation request with ID: {}", requestId);
        participationRequestService.deleteParticipationRequest(requestId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
