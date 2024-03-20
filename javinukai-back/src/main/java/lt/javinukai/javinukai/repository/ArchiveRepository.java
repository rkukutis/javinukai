package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.PastCompetitionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ArchiveRepository extends JpaRepository <PastCompetitionRecord, UUID> {

}
