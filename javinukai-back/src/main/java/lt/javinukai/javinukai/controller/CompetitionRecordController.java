package lt.javinukai.javinukai.controller;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.UserRepository;
import lt.javinukai.javinukai.service.CategoryService;
import lt.javinukai.javinukai.service.CompetitionRecordService;
import lt.javinukai.javinukai.service.ContestService;
import lt.javinukai.javinukai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
public class CompetitionRecordController {

    private final CategoryService categoryService;
    private final ContestService contestService;
    private final CompetitionRecordService competitionRecordService;
    private final UserService userService;

    @Autowired
    public CompetitionRecordController(CategoryService categoryService,
                                       ContestService contestService,
                                       CompetitionRecordService competitionRecordService,
                                       UserService userService) {
        this.categoryService = categoryService;
        this.contestService = contestService;
        this.competitionRecordService = competitionRecordService;
        this.userService = userService;
    }

    @GetMapping(path = "/enter")
    public ResponseEntity<List<CompetitionRecord>> retrieveAllRecords() {
        final List<CompetitionRecord> competitionRecordList = competitionRecordService.retrieveAllCompetitionRecords();
        log.info("Request for retrieving all competition records");
        return new ResponseEntity<>(competitionRecordList, HttpStatus.OK);
    }

//    @GetMapping(path = "/enter/{userID}")
//    public ResponseEntity<List<CompetitionRecord>> retrieveRecordsByUserID(@PathVariable @NotNull UUID userID) {
//        log.info("Request for retrieving records for user ID: {}", userID);
//        final List<CompetitionRecord> competitionRecordList = competitionRecordService.retrieveCompetitionRecordsByUserID(userID);
//        return new ResponseEntity<>(competitionRecordList, HttpStatus.OK);
//    }





//    @PostMapping(path = "/enter")
//    public ResponseEntity<CompetitionRecord> enterCompetition() {
//
//        final List<Contest> contestList = contestService.retrieveAllContests(0, 10).getContent();
//        final UUID contestID = contestList.get(0).getId();
//
//        final List<User> userList = userService.getUsers();
//        final UUID userID = userList.get(0).getUuid();
//
//        long totalSubmissions = contestList.get(0).getTotalSubmissions();
//
//        CompetitionRecord competitionRecord = competitionRecordService.testServiceMethod(contestID, userID, totalSubmissions);
//        return new ResponseEntity<>(competitionRecord, HttpStatus.OK);
//    }






}
