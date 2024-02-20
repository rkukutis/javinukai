package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.CompetitionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompetitionRecordRepository extends JpaRepository<CompetitionRecord, UUID> {
}
