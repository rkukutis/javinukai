package lt.javinukai.javinukai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CompetitionRecordDTO;
import lt.javinukai.javinukai.dto.response.CategoryCreationResponse;
import lt.javinukai.javinukai.dto.response.CompetitionRecordResponse;
import lt.javinukai.javinukai.dto.response.ParticipatingUser;
import lt.javinukai.javinukai.dto.response.UserParticipationResponse;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.service.CompetitionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                                              @AuthenticationPrincipal User participant) {

        final UserParticipationResponse userParticipationResponse = competitionRecordService
                .createUsersCompetitionRecords(contestID, participant.getId());
        final List<CompetitionRecord> usersCompetitionRecords = userParticipationResponse.getRecords();
        final HttpStatus httpStatus = userParticipationResponse.getHttpStatus();
        final String message = userParticipationResponse.getMessage();
        log.info(message);
        return new ResponseEntity<>(usersCompetitionRecords, httpStatus);
    }

    // this endpoint is for the participant to check his own options
    @GetMapping(path = "/records")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER', 'ROLE_JURY')")
    public ResponseEntity<Page<CompetitionRecord>> retrieveUserCompetitionRecords(@AuthenticationPrincipal User participant,
                                                                                  @RequestParam(defaultValue = "0") int pageNumber,
                                                                                  @RequestParam(defaultValue = "25") int pageSize,
                                                                                  @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                                  @RequestParam(defaultValue = "false") boolean sortDesc) {
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return ResponseEntity.ok().body(competitionRecordService
                .retrieveAllUserCompetitionRecords(pageable, participant.getId())
        );
    }

    @GetMapping(path = "records/contest/{contestId}/category/{categoryId}/my-record")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER', 'ROLE_JURY')")
    public ResponseEntity<CompetitionRecord> retrieveSpecificUserRecord(@PathVariable UUID contestId,
                                                                        @PathVariable UUID categoryId,
                                                                        @AuthenticationPrincipal User participant) {
        CompetitionRecord record = competitionRecordService
                .retrieveUserCompetitionRecord(categoryId, contestId, participant.getId());
        log.info("Retrieving competition record {}", record.getId());
        return ResponseEntity.ok().body(record);
    }

    // this one is for the jury
    @GetMapping(path = "/records/contest/{contestId}/category/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_JURY')")
    public ResponseEntity<Page<CompetitionRecord>> retrieveRecords(@PathVariable UUID contestId,
                                                                      @PathVariable UUID categoryId,
                                                                      @RequestParam(defaultValue = "1") int pageNumber,
                                                                      @RequestParam(defaultValue = "25") int pageSize,
                                                                      @RequestParam(required = false) String keyword,
                                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                      @RequestParam(defaultValue = "false") boolean sortDesc) {

        log.info("Request for retrieving competition {} category {} records", contestId, categoryId);
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        final Pageable pageable = PageRequest.of(--pageNumber, pageSize, sort);
        final Page<CompetitionRecord> page = competitionRecordService.retrieveCompetitionRecords(
                pageable, keyword, contestId, categoryId);
        log.info("Request for retrieving competition {} category {} competition records, {} records found",
                contestId, categoryId, page.getTotalElements());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping(path = "/records/competing/{contestID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> retrieveStillCompetingUsers(@PathVariable @NotNull UUID contestID,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "25") int limit,
                                                         @RequestParam(required = false) String contains,
                                                         @RequestParam(defaultValue = "email") String sortBy,
                                                         @RequestParam(defaultValue = "false") boolean sortDesc) {

        log.info("Request for retrieving still competing participants for competition {}", contestID);
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        final Pageable pageable = PageRequest.of(page, limit, sort);
        final Page<ParticipatingUser> participatingUsers = competitionRecordService
                .retrieveCompetingParticipants(pageable, contestID, contains);
        return new ResponseEntity<>(participatingUsers, HttpStatus.OK);
    }

    @PatchMapping(path = "/records/{recordID}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<CompetitionRecord> updateRecord(@PathVariable @NotNull UUID recordID,
                                                          @RequestBody @Valid CompetitionRecordDTO recordDTO) {
        log.info(recordDTO.toString());
        log.info("Request for updating competition record with ID: {}", recordID);
        final CompetitionRecord updatedRecord = competitionRecordService
                .updateCompetitionRecord(recordID, recordDTO);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    @DeleteMapping(path = "/records/{recordID}")
    public ResponseEntity<?> deleteRecord(@PathVariable @NotNull UUID recordID) {
        log.info("Request for deleting record with ID: {}", recordID);
        competitionRecordService.deleteRecord(recordID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}