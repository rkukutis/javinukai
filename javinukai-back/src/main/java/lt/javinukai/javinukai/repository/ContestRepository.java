package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ContestRepository extends JpaRepository<Contest, UUID> {
    Page<Contest> findByNameContainingIgnoreCase(Pageable pageable, String contestName);
}
