package lt.javinukai.javinukai.service;

import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContestServiceTest {
    @Mock
    private ContestRepository contestRepository;
    @InjectMocks
    private ContestService underTest;

    @Test
    void testCreateContestService() {
        ContestDTO contestDTO = ContestDTO.builder()
                .name("viltis")
                .description("paskutinė, nepabėgusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA"))
                .totalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        Contest createdContest = Contest.builder()
                .name("viltis")
                .description("paskutinė, nepabėgusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA"))
                .totalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        when(contestRepository.save(any(Contest.class))).thenReturn(createdContest);
        Contest result = underTest.createContest(contestDTO);

        assertNotNull(result);
        assertNotNull(createdContest);
        assertEquals(createdContest, result);

        verify(contestRepository, times(1)).save(any(Contest.class));
    }

    @Test
    void testRetrieveAllContestsService() {
        List<Contest> contestList = new ArrayList<>();

        when(contestRepository.findAll()).thenReturn(contestList);
        List<Contest> result = underTest.retrieveAllContests();

        assertNotNull(result);
        assertEquals(contestList, result);

        verify(contestRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveSpecifiedContestService() {
        UUID id = UUID.randomUUID();

        Contest expectedContest = Contest.builder()
                .name("viltis")
                .description("paskutinė, nepabegusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA"))
                .totalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        when(contestRepository.findById(eq(id))).thenReturn(Optional.of(expectedContest));
        Contest result = underTest.retrieveContest(id);

        assertNotNull(result);
        assertEquals(expectedContest, result);

        verify(contestRepository, times(1)).findById(eq(id));
    }

    @Test
    void testUpdateContestService() {
        UUID id = UUID.randomUUID();

        ContestDTO contestDTO = ContestDTO.builder()
                .name("viltis")
                .description("paskutinė, nepabėgusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA"))
                .totalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        Contest existingContest = Contest.builder()
                .name("viltis")
                .description("paskutinė, nepabėgusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA"))
                .totalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        Contest updatedContest = Contest.builder()
                .name("(ne)viltis")
                .description("paskutinė, nepabėgusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA","RELIGIJA","GAMTA","KATASTROFA"))
                .totalSubmissions(696)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        when(contestRepository.findById(eq(id))).thenReturn(Optional.of(existingContest));
        when(contestRepository.save(any(Contest.class))).thenReturn(updatedContest);

        Contest result = underTest.updateContest(id, contestDTO);

        assertNotNull(result);
        assertEquals(updatedContest, result);

        verify(contestRepository, times(1)).findById(eq(id));
        verify(contestRepository, times(1)).save(any(Contest.class));
    }

    @Test
    void testDeleteContestService() {
        UUID contestId = UUID.randomUUID();

        when(contestRepository.existsById(eq(contestId))).thenReturn(true);
        assertDoesNotThrow(() -> underTest.deleteContest(contestId));

        verify(contestRepository, times(1)).existsById(eq(contestId));
        verify(contestRepository, times(1)).deleteById(eq(contestId));
    }

}