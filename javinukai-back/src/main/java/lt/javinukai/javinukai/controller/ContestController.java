package lt.javinukai.javinukai.controller;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.service.IContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class ContestController {

    private final IContestService contestService;

    @Autowired
    public ContestController(IContestService contestService) {
        this.contestService = contestService;
    }

    @PostMapping(path = "/contests")
    public ResponseEntity<ContestDTO> createContest(@RequestBody ContestDTO contestDTO) {
        final ContestDTO createdContest = contestService.createContest(contestDTO);
        return new ResponseEntity<>(createdContest, HttpStatus.CREATED);
    }

    @GetMapping(path = "/contests")
    public ResponseEntity<List<ContestDTO>> retrieveAllContests() {
        final List<ContestDTO> contestDTOList = contestService.retrieveAllContests();
        return new ResponseEntity<>(contestDTOList, HttpStatus.OK);
    }

//    @GetMapping(path = "/contests/{id}")
//    public Optional<ResponseEntity<ContestDTO>> retrieveContest(@PathVariable UUID id) {
//        final Optional<ContestDTO> foundContest = contestService.retrieveContest(id);
//        return new ResponseEntity<>(contestDTO, HttpStatus.OK);
//    }


    @PutMapping(path = "/contests")
    public ResponseEntity<ContestDTO> updateContest(@RequestBody ContestDTO contestDTO) {
        final ContestDTO updatedContest = contestService.updateContest(contestDTO);
        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }

    @DeleteMapping(path = "/contests")
    public ResponseEntity<ContestDTO> deleteContest(@RequestBody ContestDTO contestDTO) {
        contestService.deleteContest(contestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
