package lt.javinukai.javinukai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<Contest> createContest(@RequestBody @Valid ContestDTO contestDTO) {
        final Contest createdContest = contestService.createContest(contestDTO);
        log.info("Request for contest creation completed, given ID: {}", createdContest.getId());
        return new ResponseEntity<>(createdContest, HttpStatus.CREATED);
    }

    @GetMapping(path = "/contests")
    public ResponseEntity<List<Contest>> retrieveAllContests() {
        final List<Contest> contestList = contestService.retrieveAllContests();
        log.info("Request for retrieving all contests, {} record(s) found", contestList.size());
        return new ResponseEntity<>(contestList, HttpStatus.OK);
    }

    @GetMapping(path = "/contests/{id}")
    public ResponseEntity<Contest> retrieveContest(@PathVariable @NotNull UUID id) {
        log.info("Request for retrieving contest with ID: {}", id);
        final Contest foundContest = contestService.retrieveContest(id);
        return new ResponseEntity<>(foundContest, HttpStatus.OK);
    }

    @PutMapping(path = "/contests/{id}")
    public ResponseEntity<Contest> updateContest(@PathVariable @NotNull UUID id, @RequestBody @Valid ContestDTO contestDTO) {
        log.info("Request for updating contest with ID: {}", id);
        final Contest updatedContest = contestService.updateContest(id, contestDTO);
        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }

    @DeleteMapping(path = "/contests/{id}")
    public ResponseEntity<?> deleteContest(@PathVariable @NotNull UUID id) {
        log.info("Request for deleting contest with ID: {}", id);
        contestService.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
