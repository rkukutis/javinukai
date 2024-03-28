package lt.javinukai.javinukai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.service.RateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;


    @PatchMapping("/addLike")
    @PreAuthorize("hasAuthority('ROLE_JURY')")
    public ResponseEntity<?> giveLike(@AuthenticationPrincipal User jury,
                                      @RequestParam UUID collectionId) {
        rateService.rateLike(jury.getId(), collectionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/removeLike")
    @PreAuthorize("hasAuthority('ROLE_JURY')")
    public ResponseEntity<?> removeLike(@AuthenticationPrincipal User jury,
                                        @RequestParam UUID collectionId) {
        rateService.undoLike(jury.getId(), collectionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getbycontest")
    public List<PhotoCollection> getby (@RequestParam UUID contestId){

        return rateService.findAllCollectionsInContest(contestId);
    }

    @GetMapping("/findcollections")
    public List<PhotoCollection> finddd(@RequestParam UUID juryId) {
        return rateService.findCollectionsLikedByJury(juryId);
    }
}
