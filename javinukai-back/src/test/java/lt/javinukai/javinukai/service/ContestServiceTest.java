package lt.javinukai.javinukai.service;


import com.sun.tools.jconsole.JConsoleContext;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.ContestDTO;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.enums.PhotoSubmissionType;
import lt.javinukai.javinukai.mapper.ContestMapper;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ContestServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ContestRepository contestRepository;
    @InjectMocks
    private ContestService contestService;
/*
    @Test
    public void retrieveAllContestsReturnsPageOfContests() {
        final Contest contest01 = Contest.builder()
                .name("test contest name")
                .description("test contest description")
                .maxTotalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        final List<Contest> expectedContestList = new ArrayList<>();
        expectedContestList.add(contest01);

        Page<Contest> expectedPage = new PageImpl<>(expectedContestList);

        when(contestRepository.findAll(Mockito.any(Pageable.class))).thenReturn(expectedPage);

        Page<Contest> retrievedPage = contestService.retrieveAllContests(Pageable.unpaged(), null);

        assertNotNull(retrievedPage);
        assertEquals(expectedContestList.size(), retrievedPage.getContent().size());
    }
*/
    @Test
    void retrieveContestReturnsContest() {
        UUID id = UUID.randomUUID();

        Category category01 = Category.builder()
                .name("gamta")
                .description("ir taip aišku ")
                .maxTotalSubmissions(100)
                .build();

        Contest expectedContest = Contest.builder()
                .name("viltis")
                .description("paskutinė, nepabegusi")
                .categories(Collections.singletonList(category01))
                .maxTotalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        when(contestRepository.findById(eq(id))).thenReturn(Optional.of(expectedContest));
        Contest result = contestService.retrieveContest(id);

        assertNotNull(result);
        assertEquals(expectedContest, result);

        verify(contestRepository, times(1)).findById(eq(id));
    }
/*
    @Test
    void updateCategoriesOfContestReturnsContest() {

        final UUID categoryID = UUID.randomUUID();
        final Category category = new Category(categoryID,
                "test category name",
                "testCategory description",
                66,
                30,
                null, null, null, null, null, PhotoSubmissionType.SINGLE);
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.findById(categoryID)).thenReturn(Optional.of(category));

        final UUID contestID = UUID.randomUUID();
        final Contest initialContest = new Contest(contestID,
                "test contest name",
                "test contest description",
                null,
                null,
                null,
                666, 50, null, null, null, null);

        when(contestRepository.findById(contestID)).thenReturn(Optional.ofNullable(initialContest));
        when(contestRepository.save(any(Contest.class))).thenReturn(initialContest);

        final Contest updatedContest = contestService.updateCategoriesOfContest(initialContest.getId(), categories);

        Assertions.assertThat(updatedContest).isNotNull();
        Assertions.assertThat(updatedContest.getId()).isNotNull();
    }
*/
    /*
    @Test
    void deleteContestSuccess() {
        UUID contestId = UUID.randomUUID();

        when(contestRepository.existsById(eq(contestId))).thenReturn(true);
        assertDoesNotThrow(() -> contestService.deleteContest(contestId));

        verify(contestRepository, times(1)).existsById(eq(contestId));
        verify(contestRepository, times(1)).deleteById(eq(contestId));
    }
     */

    @Test
    void deleteContestFail() {
        UUID contestId = UUID.randomUUID();

        when(contestRepository.existsById(eq(contestId))).thenReturn(false);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contestService.deleteContest(contestId);
        });

        Assertions.assertThat(exception.getMessage()).
                isEqualTo("Contest was not found with ID: " + contestId);
    }

}