package lt.javinukai.javinukai.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.ParticipationRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ParticipationRequestStatus;
import lt.javinukai.javinukai.service.ParticipationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Page<ParticipationRequest>> findAllRequests(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                                                      @RequestParam(defaultValue = "2") @Min(0) Integer listSize,
                                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                      @RequestParam(defaultValue = "false") boolean sortDesc,
                                                                      @RequestParam(required = false) String contains) {
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(page, listSize, sort);
        return ResponseEntity.ok().body(participationRequestService.getAllRequests(pageRequest, contains));
    }


    @PostMapping("/request")
    public ParticipationRequest createParticipationRequest(@AuthenticationPrincipal User user,
                                                           @RequestParam UUID contestId) {
        ParticipationRequest newRequest = participationRequestService.createParticipationRequest(contestId, user.getId());
        return newRequest;
    }

    @PatchMapping("/request/{requestId}")
    public ResponseEntity<ParticipationRequest> updateRequestStatus(@PathVariable UUID requestId,
                                                                    @RequestParam String participationStatus) {
        return ResponseEntity.ok().body(participationRequestService.updateRequestStatus(requestId, ParticipationRequestStatus.valueOf(participationStatus.toUpperCase())));
    }

    @DeleteMapping(path = "/request/{requestId}")
    public ResponseEntity<?> deleteContest(@PathVariable @NotNull UUID requestId) {
        log.info("Request for deleting participation request with ID: {}", requestId);
        participationRequestService.deleteParticipationRequest(requestId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/request")
    public ParticipationRequest getRequest(@AuthenticationPrincipal User user,
                                           @RequestParam UUID contestId) {
        List<ParticipationRequest> participationList = participationRequestService.getRequestByUserIdAndContestId(user.getId(), contestId);
        if (participationList.isEmpty()) {
            return null;
        }
        return participationList.get(0);
    }
}
