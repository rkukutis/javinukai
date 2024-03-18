package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Category findByNameAndDescriptionAndMaxTotalSubmissions(String categoryName, String description, long totalSubmissions);
    Page<Category> findByNameContainingIgnoreCase(String categoryName, Pageable pageable);
    Page<Category> findByName(String s, Pageable any);
}
