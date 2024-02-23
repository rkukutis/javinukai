package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CompetitionRecordService {

    private final CategoryRepository categoryRepository;
    private final ContestRepository contestRepository;
    private final CompetitionRecordRepository competitionRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public CompetitionRecordService (CategoryRepository categoryRepository,
                                     ContestRepository contestRepository,
                                     CompetitionRecordRepository competitionRecordRepository,
                                     UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.contestRepository = contestRepository;
        this.competitionRecordRepository = competitionRecordRepository;
        this.userRepository = userRepository;
    }

    public List<CompetitionRecord> retrieveAllCompetitionRecords() {
        final List<CompetitionRecord> competitionRecordList = competitionRecordRepository.findAll();
        log.info("{}: Retrieving all competition record list from database", this.getClass().getName());
        return competitionRecordList;
    }

    public List<CompetitionRecord> retrieveCompetitionRecordsByUser(User user) {
        final List<CompetitionRecord> competitionRecordList = competitionRecordRepository.findByUser(user);
        log.info("Retrieving competition records from database, name - {}", this.getClass().getName());
        return competitionRecordList;
    }


}
