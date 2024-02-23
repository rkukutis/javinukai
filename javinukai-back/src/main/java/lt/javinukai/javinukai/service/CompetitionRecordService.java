package lt.javinukai.javinukai.service;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.exception.UserNotFoundException;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import lt.javinukai.javinukai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<CompetitionRecord> retrieveAllCompetitionRecords(Pageable pageable, String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            log.info("{}: Retrieving all competition records from database", this.getClass().getName());
            return competitionRecordRepository.findAll(pageable);
        } else {
            log.info("{}: Retrieving categories by use email", this.getClass().getName());
            final User userToFind = userRepository.findByEmail(keyword)
                    .orElseThrow(()-> new UserNotFoundException(keyword));
            return competitionRecordRepository.findByUser(userToFind, pageable);
        }
    }
}
