package lt.javinukai.javinukai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.service.RateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> giveLike(@AuthenticationPrincipal User jury,
                                      @RequestParam UUID collectionId) {
        rateService.rateLike(jury.getId(), collectionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/removeLike")
    public ResponseEntity<?> removeLike(@AuthenticationPrincipal User jury,
                                        @RequestParam UUID collectionId) {
        rateService.undoLike(jury.getId(), collectionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findcollections")
    public List<PhotoCollection> finddd(@RequestParam UUID collectionId) {
        return rateService.findCollections(collectionId);
    }

    @GetMapping("/findjurys")
    public List<User> fi(@RequestParam UUID collectionId) {
        return rateService.findJurys(collectionId);
    }

    @GetMapping("/exists")
    public boolean isPairExists(@RequestParam UUID juryId,
                                @RequestParam UUID collectionId) {
        return rateService.pairExists(juryId, collectionId);
    }

    @DeleteMapping("/del")
    public ResponseEntity<?> deleteAllLikes (){
        rateService.deleteAllLikes();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getall")
    public List<User> findAll() {
        return rateService.findAllWhoLiked();
    }
}
