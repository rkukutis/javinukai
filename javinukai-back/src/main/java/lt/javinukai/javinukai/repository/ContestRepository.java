package lt.javinukai.javinukai.repository;

import io.swagger.v3.core.util.AnnotationsUtils;
import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ContestRepository extends JpaRepository<Contest, UUID> {
    Page<Contest> findByNameContainingIgnoreCase(Pageable pageable, String contestName);
    List<Contest> findByOrderByCreatedAtDesc(Limit of);


    Page<Contest> findByIsArchived(Pageable pageable, boolean b);

    Page<Contest> findByNameContainingIgnoreCaseAndIsArchived(Pageable pageable, String keyword, boolean b);
}
