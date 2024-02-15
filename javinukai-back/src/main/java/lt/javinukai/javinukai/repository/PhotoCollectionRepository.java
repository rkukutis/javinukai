package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.PhotoCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhotoCollectionRepository extends JpaRepository<PhotoCollection, UUID> {
}
