package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Contest, UUID> {
}
