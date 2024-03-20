package lt.javinukai.javinukai.service;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.PastCompetition;
import lt.javinukai.javinukai.repository.ArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArchiveService {

    private final ArchiveRepository archiveRepository;

    @Autowired
    public ArchiveService(ArchiveRepository archiveRepository) {
        this.archiveRepository = archiveRepository;
    }


    public void addToArchive(CompetitionRecord competitionRecord) {

        PastCompetition pastCompetition =



        archiveRepository



    }



}
