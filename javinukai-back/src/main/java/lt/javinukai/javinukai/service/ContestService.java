package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.ContestDTO;
import lt.javinukai.javinukai.mapper.ContestMapper;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ContestService {

    private final ContestRepository contestRepository;

    @Autowired
    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    public Contest createContest(ContestDTO contestDTO) {
        final Contest contest = ContestMapper.contestDTOToContest(contestDTO);
        final Contest createdContest = contestRepository.save(contest);
        log.info("{}: Created and added new contest to database", this.getClass().getName());
        return createdContest;
    }

    public List<Contest> retrieveAllContests() {
        final List<Contest> listContests = contestRepository.findAll();
        log.info("{}: Retrieving all contest list from database", this.getClass().getName());
        return listContests;
    }

    public Contest retrieveContest(UUID id) {
        final Contest contestToShow = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        log.info("Retrieving contest from database, name - {}", this.getClass().getName());
        return contestToShow;
    }

    public Contest updateContest(UUID id, ContestDTO contestDTO) {
        final Contest contestToUpdate = contestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Contest was not found with ID: " + id));
        contestToUpdate.setName(contestDTO.getName());
        contestToUpdate.setDescription(contestDTO.getDescription());
        contestToUpdate.setCategory(contestDTO.getCategory());
        contestToUpdate.setTotalSubmissions(contestDTO.getTotalSubmissions());
        contestToUpdate.setStartDate(contestDTO.getStartDate());
        contestToUpdate.setEndDate(contestDTO.getEndDate());
        log.info("{}: Updating contest", this.getClass().getName());
        return contestRepository.save(contestToUpdate);
    }

    public void deleteContest(UUID id) {
        if (contestRepository.existsById(id)) {
            contestRepository.deleteById(id);
            log.info("{}: Deleted contest from the database with ID: {}", this.getClass().getName(), id);
        } else {
            throw new EntityNotFoundException("Contest was not found with ID: " + id);
        }
    }

}