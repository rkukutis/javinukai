package lt.javinukai.javinukai.controller;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
public class ContestController {

    private final ContestService contestService;

    @Autowired
    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @PostMapping(path = "/contests")
    public ResponseEntity<Contest> createContest(@RequestBody ContestDTO contestDTO) {
        final Contest createdContest = contestService.createContest(contestDTO);
        return new ResponseEntity<>(createdContest, HttpStatus.CREATED);
    }

    @GetMapping(path = "/contests")
    public ResponseEntity<List<Contest>> retrieveAllContests() {
        final List<Contest> contestList = contestService.retrieveAllContests();
        return new ResponseEntity<>(contestList, HttpStatus.OK);
    }

    @GetMapping(path = "/contests/{id}")
    public ResponseEntity<Contest> retrieveContest(@PathVariable UUID id) {
        log.debug("GET request for all contests");
        final Contest foundContest = contestService.retrieveContest(id);
        return new ResponseEntity<>(foundContest, HttpStatus.OK);
    }

    @PutMapping(path = "/contests/{id}")
    public ResponseEntity<Contest> updateContest(@PathVariable UUID id, @RequestBody ContestDTO contestDTO) {
        log.debug("PUT request for id - {}", id);
        final Contest updatedContest = contestService.updateContest(id, contestDTO);
        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }

    @DeleteMapping(path = "/contests/{id}")
    public ResponseEntity<ContestDTO> deleteContest(@PathVariable UUID id) {
        log.debug("DELETE request for id - {}", id);
        contestService.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
