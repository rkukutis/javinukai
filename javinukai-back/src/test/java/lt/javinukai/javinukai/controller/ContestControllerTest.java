package lt.javinukai.javinukai.controller;

import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.service.ContestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContestControllerTest {

    @Mock
    private ContestService contestService;

    @InjectMocks
    private ContestController contestController;

    @Test
    void testCreateContestController() {
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

        when(contestService.createContest(any(ContestDTO.class))).thenReturn(createdContest);
        ResponseEntity<Contest> responseEntity = contestController.createContest(contestDTO);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdContest, responseEntity.getBody());

        verify(contestService, times(1)).createContest(any(ContestDTO.class));
    }

    @Test
    void testRetrieveAllContestsController() {
        List<Contest> contestList = new ArrayList<>();

        when(contestService.retrieveAllContests()).thenReturn(contestList);
        ResponseEntity<List<Contest>> responseEntity = contestController.retrieveAllContests();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(contestList, responseEntity.getBody());

        verify(contestService, times(1)).retrieveAllContests();
    }

    @Test
    void testRetrieveSpecifiedContestController() {
        UUID id = UUID.randomUUID();

        Contest expectedContest = Contest.builder()
                .name("viltis")
                .description("paskutinė, nepabėgusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA"))
                .totalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        when(contestService.retrieveContest(eq(id))).thenReturn(expectedContest);
        ResponseEntity<Contest> responseEntity = contestController.retrieveContest(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedContest, responseEntity.getBody());

        verify(contestService, times(1)).retrieveContest(eq(id));
    }

    @Test
    void testUpdateContestController() {
        UUID id = UUID.randomUUID();

        ContestDTO contestDTO = ContestDTO.builder()
                .name("viltis")
                .description("paskutinė, nepabėgusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA"))
                .totalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        Contest updatedContest = Contest.builder()
                .name("viltis")
                .description("paskutinė, nepabėgusi")
                .category(Arrays.asList("SPORTAS", "MEDICINA", "ISTORIJA"))
                .totalSubmissions(666)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        when(contestService.updateContest(eq(id), any(ContestDTO.class))).thenReturn(updatedContest);

        ResponseEntity<Contest> responseEntity = contestController.updateContest(id, contestDTO);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedContest, responseEntity.getBody());

        verify(contestService, times(1)).updateContest(eq(id), any(ContestDTO.class));
    }

    @Test
    void testDeleteContestController() {
        UUID id = UUID.randomUUID();
        assertDoesNotThrow(() -> contestController.deleteContest(id));
        verify(contestService, times(1)).deleteContest(eq(id));
    }

}