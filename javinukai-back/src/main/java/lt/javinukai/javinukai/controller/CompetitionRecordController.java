package lt.javinukai.javinukai.controller;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.service.CategoryService;
import lt.javinukai.javinukai.service.CompetitionRecordService;
import lt.javinukai.javinukai.service.ContestService;
import lt.javinukai.javinukai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
