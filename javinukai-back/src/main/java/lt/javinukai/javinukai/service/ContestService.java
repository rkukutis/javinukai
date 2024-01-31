package lt.javinukai.javinukai.service;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.dto.ContestMapper;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ContestService implements IContestService {

    private final ContestRepository contestRepository;

    @Autowired
    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    @Override
    public ContestDTO createContest(ContestDTO contestDTO) {
        final Contest contest = ContestMapper.contestDTOToContest(contestDTO);
        final Contest createdContest = contestRepository.save(contest);
        return ContestMapper.contestToContestDTO(createdContest);
    }

    @Override
    public List<ContestDTO> retrieveAllContests() {
        final List<Contest> contestEntityList= contestRepository.findAll();
        return contestEntityList.stream()
                .map(ContestMapper::contestToContestDTO)
                .toList();
    }

    @Override
    public Optional<ContestDTO> retrieveContest(UUID id) {
        final Optional<Contest> contestFound = contestRepository.findById(id);
        return contestFound.map(ContestMapper::contestToContestDTO);
    }

    @Override
    public ContestDTO updateContest(ContestDTO contestDTO) {
        final Contest contestToUpdate = ContestMapper.contestDTOToContest(contestDTO);
        contestRepository.save(contestToUpdate);
        return contestDTO;
    }

    @Override
    public void deleteContest(ContestDTO contestDTO) {
        final Contest contestToDelete = ContestMapper.contestDTOToContest(contestDTO);
        contestRepository.deleteById(contestToDelete.getId());
    }

}