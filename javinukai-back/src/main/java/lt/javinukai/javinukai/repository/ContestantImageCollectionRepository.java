package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.ContestantImageCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContestantImageCollectionRepository extends JpaRepository<ContestantImageCollection, UUID> {
}
