package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CompetitionRecordRepository extends JpaRepository<CompetitionRecord, UUID> {

    Page<CompetitionRecord> findByUser(User user, Pageable pageable);
}
