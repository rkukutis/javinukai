package lt.javinukai.javinukai.service;

import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.entity.Contest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IContestService {
    ContestDTO createContest(ContestDTO contestDTO);
    List<ContestDTO> retrieveAllContests();
    Optional<ContestDTO> retrieveContest(UUID id);
    ContestDTO updateContest(ContestDTO contestDTO);
    void deleteContest(ContestDTO contestDTO);
}
