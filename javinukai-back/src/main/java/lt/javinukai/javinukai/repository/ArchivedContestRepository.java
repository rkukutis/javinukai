package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.PastCompetition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArchivedContestRepository extends JpaRepository<PastCompetition, UUID> {
    Page<PastCompetition> findByContestNameContainingIgnoreCase(String keyword, Pageable pageable);
}
