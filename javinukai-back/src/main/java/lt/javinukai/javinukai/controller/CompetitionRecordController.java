package lt.javinukai.javinukai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CompetitionRecordDTO;
import lt.javinukai.javinukai.dto.response.CategoryCreationResponse;
import lt.javinukai.javinukai.dto.response.CompetitionRecordResponse;
import lt.javinukai.javinukai.dto.response.UserParticipationResponse;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.service.CompetitionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
public class CompetitionRecordController {

    private final CompetitionRecordService competitionRecordService;

    @Autowired
    public CompetitionRecordController(CompetitionRecordService competitionRecordService) {
        this.competitionRecordService = competitionRecordService;
    }

    @PostMapping(path = "/records")
    public ResponseEntity<List<CompetitionRecord>> addUserToContest(@RequestParam UUID contestID,
                                              @RequestParam UUID userID) {

        final UserParticipationResponse userParticipationResponse = competitionRecordService
                .createUsersCompetitionRecords(contestID, userID);
        final List<CompetitionRecord> usersCompetitionRecords = userParticipationResponse.getRecords();
        final HttpStatus httpStatus = userParticipationResponse.getHttpStatus();
        final String message = userParticipationResponse.getMessage();

        log.info(message);
        return new ResponseEntity<>(usersCompetitionRecords, httpStatus);
    }

//    @PostMapping(path = "/records")
//    public ResponseEntity<List<CompetitionRecordResponse>> addUserToContest(@RequestParam UUID contestID,
//                                                                            @RequestParam UUID userID) {
//
//        final List<CompetitionRecordResponse> usersCompetitionRecords =
//                competitionRecordService.createUsersCompetitionRecords(contestID, userID);
//
//        return new ResponseEntity<>(usersCompetitionRecords, HttpStatus.CREATED);
//    }

    @GetMapping(path = "/records")
    public ResponseEntity<Page<CompetitionRecord>> retrieveAllRecords(@RequestParam(defaultValue = "1") int pageNumber,
                                                                      @RequestParam(defaultValue = "25") int pageSize,
                                                                      @RequestParam(required = false) String keyword,
                                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                      @RequestParam(defaultValue = "false") boolean sortDesc) {

        log.info("Request for retrieving all competition records");
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        final Pageable pageable = PageRequest.of(--pageNumber, pageSize, sort);
        final Page<CompetitionRecord> page = competitionRecordService.retrieveAllCompetitionRecords(pageable, keyword);
        log.info("Request for retrieving all competition records, {} records found", page.getTotalElements());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

//    @GetMapping(path = "/records")
//    public ResponseEntity<Page<CompetitionRecordResponse>> retrieveAllRecords(@RequestParam(defaultValue = "1") int pageNumber,
//                                                                      @RequestParam(defaultValue = "25") int pageSize,
//                                                                      @RequestParam(required = false) String keyword,
//                                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
//                                                                      @RequestParam(defaultValue = "false") boolean sortDesc) {
//
//        log.info("Request for retrieving all competition records");
//        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Sort sort = Sort.by(direction, sortBy);
//
//        final Pageable pageable = PageRequest.of(--pageNumber, pageSize, sort);
//        final Page<CompetitionRecordResponse> page = competitionRecordService.retrieveAllCompetitionRecords(pageable, keyword);
//        log.info("Request for retrieving all competition records, {} records found", page.getTotalElements());
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }

    @PatchMapping(path = "/records/{recordID}")
    public ResponseEntity<CompetitionRecord> updateRecord(@PathVariable @NotNull UUID recordID,
                                                          @RequestBody @Valid CompetitionRecordDTO recordDTO) {
        log.info("Request for updating competition record with ID: {}", recordID);
        final CompetitionRecord updatedRecord = competitionRecordService
                .updateCompetitionRecord(recordID, recordDTO);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    /*
    v1 - ištrina atskirą įrašą, nemanau, kad taip galima daryti
    nežinau ar DELETE išvis reikalingas, gal užteks, kad pašalinus user, category, contest
    jis bus pašalintas per kaskadą
//     */
//    @DeleteMapping(path = "/records/{recordID}")
//    public ResponseEntity<?> deleteRecord(@PathVariable @NotNull UUID recordID) {
//        log.info("Request for deleting record with ID: {}", recordID);
//        competitionRecordService.deleteRecord(recordID);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}