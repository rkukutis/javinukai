package lt.javinukai.javinukai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.service.ContestService;
import lt.javinukai.javinukai.wrapper.ContestWrapper;
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
public class ContestController {
    private final ContestService contestService;

    @Autowired
    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @PostMapping(path = "/contests")
//    public ResponseEntity<Contest> createContest(@RequestBody @Valid ContestDTO contestDTO,
//                                                 @AuthenticationPrincipal UserDetails userDetails) {
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Contest> createContest(@RequestBody @Valid ContestDTO contestDTO) {
        final Contest createdContest = contestService.createContest(contestDTO);
        log.info("Request for contest creation completed, given ID: {}", createdContest.getId());
        return new ResponseEntity<>(createdContest, HttpStatus.CREATED);
    }

    @GetMapping(path = "/contests")
    public ResponseEntity<Page<Contest>> retrieveAllContests(
                                                             @RequestParam(defaultValue = "1") int pageNumber,
                                                             @RequestParam(defaultValue = "25") int pageSize,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(defaultValue = "name") String sortBy,
                                                             @RequestParam(defaultValue = "false") boolean sortDesc) {

        log.info("Request for retrieving all contests");
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        final Pageable pageable = PageRequest.of(--pageNumber, pageSize, sort);
        final Page<Contest> page = contestService.retrieveAllContests(pageable, name);
        log.info("Request for retrieving all contests, {} record(s) found", page.getTotalElements());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping(path = "/contests/{id}")
    public ResponseEntity<Contest> retrieveContest(@PathVariable @NotNull UUID id) {
        log.info("Request for retrieving contest with ID: {}", id);
        final Contest foundContest = contestService.retrieveContest(id);
        return new ResponseEntity<>(foundContest, HttpStatus.OK);
    }


    @GetMapping(path = "/contests/{id}/info")
    public ResponseEntity<ContestWrapper> retrieveContest(@PathVariable @NotNull UUID id, @AuthenticationPrincipal User user) {
        log.info("Request for retrieving contest with ID: {} and additional information", id);
        final ContestWrapper contestWrapper = contestService.retrieveContest(id, user);
        return new ResponseEntity<>(contestWrapper, HttpStatus.OK);
    }

    @PutMapping(path = "/contests/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Contest> updateContest(@PathVariable @NotNull UUID id,
                                                 @RequestBody @Valid ContestDTO contestDTO) {
        log.info("Request for updating contest with ID: {}", id);
        final Contest updatedContest = contestService.updateContest(id, contestDTO);
        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }

    @PatchMapping(path = "/contests/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Contest> patchCategories(@PathVariable @NotNull UUID id,
                                                  @RequestBody @Valid List<Category> categories) {
        log.info("Request for patching contest with ID: {}", id);
        final Contest updatedContest = contestService.updateCategoriesOfContest(id, categories);
        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }

    @DeleteMapping(path = "/contests/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteContest(@PathVariable @NotNull UUID id) {
        log.info("Request for deleting contest with ID: {}", id);
        contestService.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PatchMapping(path = "/contests/{id}/reset")
    public ResponseEntity<?> startNewStage (@PathVariable UUID contestId){
        contestService.startNewCompetitionStage(contestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}