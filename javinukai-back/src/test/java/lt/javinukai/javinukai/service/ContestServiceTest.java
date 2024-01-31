package lt.javinukai.javinukai.service;

import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.dto.ContestMapper;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContestServiceTest {
    @Mock
    private ContestRepository contestRepository;
    @InjectMocks
    private ContestService underTest;

    @Test
    public void testThatContestIsCreated() {

        final Contest contest = Contest.builder()
                .name("viltis")
                .description("paskutine, nepabegusi")
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();
    }
}