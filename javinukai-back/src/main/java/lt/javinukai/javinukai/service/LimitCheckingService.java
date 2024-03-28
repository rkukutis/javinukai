package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitCheckingService {
    private final PhotoCollectionRepository collectionRepository;

    // return boolean if user total limit per contest not reached
    public boolean checkUserContestLimit (CompetitionRecord competitionRecord) {

        int totalUserContestEntries = collectionRepository
                .findCollectionsByContestIdAndUserId(
                        competitionRecord.getContest().getId(), competitionRecord.getUser().getId()
                ).size();

        if (competitionRecord.getUser().isCustomLimits()) {
            return (competitionRecord.getUser().getMaxTotal() - totalUserContestEntries) > 0;
        } else {
            long dif = competitionRecord.getContest().getMaxUserSubmissions() - totalUserContestEntries;
            return dif > 0;
        }
    }

    public boolean checkUserCategoryLimit(CompetitionRecord competitionRecord) {
        int categoryEntryNumber = competitionRecord.getEntries().size();
        return (competitionRecord.getMaxPhotos() - categoryEntryNumber) > 0;
    }

    // each contest has a number of total allowed submissions, return false if adding one more goes over the limit
    public boolean checkContestLimit(CompetitionRecord record) {
        int totalContestEntries = collectionRepository
                .findCollectionsByContestId(record.getContest().getId()).size();
        return totalContestEntries + 1 <= record.getContest().getMaxTotalSubmissions();
    }

    // the same for the category limit
    public boolean checkCategoryLimit(CompetitionRecord record) {
        Contest contest = record.getContest();
        Category category = record.getCategory();

        int totalCategoryEntries = collectionRepository
                .findCollectionsByContestIdAndCategoryId(
                        record.getContest().getId(), record.getCategory().getId()
                ).size();

        return totalCategoryEntries + 1 <= category.getMaxTotalSubmissions();
    }
}
