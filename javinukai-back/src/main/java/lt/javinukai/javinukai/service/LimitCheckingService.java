package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LimitCheckingService {
    private final CompetitionRecordRepository recordRepository;


    // return boolean if user total limit per contest not reached
    public boolean checkUserContestLimit (CompetitionRecord competitionRecord) {
        List<CompetitionRecord> allRecords = recordRepository.findByContestIdAndUserId(
                competitionRecord.getContest().getId(), competitionRecord.getUser().getId()
        );
        long allEntriesNumber = allRecords.stream()
                .map(r -> Long.valueOf(r.getEntries().size()))
                .reduce(Long::sum).orElse(0L);
        return (competitionRecord.getUser().getMaxTotal() - allEntriesNumber) > 0 ;
    }

    public boolean checkUserCategoryLimit(CompetitionRecord competitionRecord) {
        int categoryEntryNumber = competitionRecord.getEntries().size();
        boolean result = false;
        switch (competitionRecord.getCategory().getType()) {
            case SINGLE -> result = (competitionRecord.getUser().getMaxSinglePhotos() - categoryEntryNumber) > 0;
            case COLLECTION -> result = (competitionRecord.getUser().getMaxCollections() - categoryEntryNumber) > 0;
        }
        return result;
    }
}
