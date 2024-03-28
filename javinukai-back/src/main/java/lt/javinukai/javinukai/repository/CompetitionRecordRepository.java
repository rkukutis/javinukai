package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CompetitionRecordRepository extends JpaRepository<CompetitionRecord, UUID> {

    Page<CompetitionRecord> findByUser(User user, Pageable pageable);
    Page<CompetitionRecord> findByUserId(Pageable pageable, UUID userId);
    List<CompetitionRecord> findByUserAndContest(User user, Contest contest);
    Page<CompetitionRecord> findByCategoryIdAndContestId(Pageable pageable, UUID categoryId, UUID contestId);
    Optional<CompetitionRecord> findByCategoryIdAndContestIdAndUserId(UUID categoryId, UUID contestId, UUID userId);
    List<CompetitionRecord> findByContestIdAndUserId(UUID contestId, UUID userId);
    List<CompetitionRecord> findByContestId(UUID id);
    List<CompetitionRecord> findByContestIdAndCategoryId(UUID id, UUID id1);
    void deleteByCategoryIdAndContestId(UUID id, UUID contestId);
}
