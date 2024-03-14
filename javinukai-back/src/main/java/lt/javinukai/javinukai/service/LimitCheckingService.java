package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return (competitionRecord.getMaxPhotos() - categoryEntryNumber) > 0;
    }

    // each contest has a number of total allowed submissions, return false if adding one more goes over the limit
    public boolean checkContestLimit(CompetitionRecord record) {
        List<CompetitionRecord> totalRecords = recordRepository.findByContestId(record.getContest().getId());
        long totalEntryNumber = totalRecords.stream()
                .map(r -> r.getEntries().size())
                .reduce(Integer::sum).orElse(0);
        return totalEntryNumber + 1 <= record.getContest().getMaxSubmissions();
    }

    // the same for the category limit
    public boolean checkCategoryLimit(CompetitionRecord record) {
        Contest contest = record.getContest();
        Category category = record.getCategory();
        List<CompetitionRecord> totalRecords = recordRepository.findByContestIdAndCategoryId(
                contest.getId(), category.getId()
        );
        long totalEntryNumber = totalRecords.stream()
                .map(r -> r.getEntries().size())
                .reduce(Integer::sum).orElse(0);
        return totalEntryNumber + 1 <= category.getMaxSubmissions();
    }
}
