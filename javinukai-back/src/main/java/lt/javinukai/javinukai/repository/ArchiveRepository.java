package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.PastCompetition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArchiveRepository extends JpaRepository <PastCompetition, UUID> {

}
