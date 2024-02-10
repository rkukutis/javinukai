package lt.javinukai.javinukai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.CategoryDTO;
import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.service.CategoryService;
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
    private final CategoryService categoryService;
    private final ContestService contestService;

    @Autowired
    public ContestController(ContestService contestService, CategoryService categoryService) {
        this.contestService = contestService;
        this.categoryService = categoryService;
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

///////////////////////////////////////////////////////////////////////////////////////////////////////
// v1 /////////////////
    @PatchMapping(path = "/contests/add/{id}")
    public ResponseEntity<Contest> addCategory(@PathVariable @NotNull UUID id,
                                               @RequestBody @Valid List<UUID> categories) {
        log.info("Request for patching contest by adding new categories with ID: {}", id);
        final Contest updatedContest = contestService.addCategories(id, categories);
        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }

    @PatchMapping(path = "/contests/remove/{id}")
    public ResponseEntity<Contest> removeCategory(@PathVariable @NotNull UUID id,
                                               @RequestBody @Valid List<UUID> categories) {
        log.info("Request for patching contest by adding new categories with ID: {}", id);
        final Contest updatedContest = contestService.removeCategories(id, categories);
        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }
// v2 ////////////////
    @PatchMapping(path = "/contests/{id}")
    public ResponseEntity<Contest> patchCategories(@PathVariable @NotNull UUID id,
                                                  @RequestBody @Valid List<Category> categories) {
        log.info("Request for patching contest with ID: {}", id);
        final Contest updatedContest = contestService.updateCategoriesOfContest(id, categories);
        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////

    @DeleteMapping(path = "/contests/{id}")
    public ResponseEntity<?> deleteContest(@PathVariable @NotNull UUID id) {
            log.info("Request for deleting contest with ID: {}", id);
        contestService.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/categories")
    public ResponseEntity<Category> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        final Category createdCategory = categoryService.createCategory(categoryDTO);
        if (createdCategory != null) {
            log.info("Request for category creation completed, given ID: {}", createdCategory.getId());
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        }
        log.info("Request for category creation completed, already in repo");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping(path = "/categories")
    public ResponseEntity<List<Category>> retrieveAllCategories(@RequestParam(defaultValue = "0") int pageNumber,
                                                                @RequestParam(defaultValue = "19") int pageSize) {
        final List<Category> categoryList = categoryService.retrieveAllCategories(pageNumber, pageSize);
        log.info("Request for retrieving all categories, {} record(s) found", categoryList.size());
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}")
    public ResponseEntity<Category> retrieveCategory(@PathVariable @NotNull UUID id) {
        log.info("Request for retrieving category with ID: {}", id);
        final Category category = categoryService.retrieveCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping(path = "/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable @NotNull UUID id, @RequestBody @Valid CategoryDTO categoryDTO) {
        log.info("Request for updating category with ID: {}", id);
        final Category categoryToUpdate = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(categoryToUpdate, HttpStatus.OK);
    }

    @DeleteMapping(path = "/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable @NotNull UUID id) {
        log.info("Request for deleting category with ID: {}", id);
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}