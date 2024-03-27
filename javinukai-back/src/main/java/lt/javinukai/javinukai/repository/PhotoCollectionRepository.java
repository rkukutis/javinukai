package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.PhotoCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PhotoCollectionRepository extends JpaRepository<PhotoCollection, UUID> {
    @Query("SELECT p FROM PhotoCollection p JOIN p.juryLikes l WHERE l.id = ?1")
    List<PhotoCollection> findLikedCollectionsByJuryId(UUID juryId);

    @Query("SELECT p FROM PhotoCollection p JOIN p.juryLikes l WHERE l.id = ?1 AND p.id = ?2")
    PhotoCollection findByUserIdAndPhotoCollectionId(UUID juryId, UUID photoCollectionId);

    @Query("SELECT p FROM PhotoCollection p JOIN p.competitionRecord c WHERE c.contest = ?1")
    List<PhotoCollection> findCollectionsInContest (Contest contest);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION" +
            " JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID" +
            " WHERE COMPETITION_RECORD.CONTEST_ID = ?1 AND COMPETITION_RECORD.CATEGORY_ID = ?2 AND PHOTO_COLLECTION.LIKES_COUNT > ?3" +
            " AND HIDDEN_FROM_JURY = FALSE")
    Page<PhotoCollection> findVisibleByLikesCountGreaterThan(UUID competitionId, UUID categoryId, int i, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION" +
            " JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID" +
            " WHERE COMPETITION_RECORD.CONTEST_ID = ?1 AND COMPETITION_RECORD.CATEGORY_ID = ?2 AND PHOTO_COLLECTION.LIKES_COUNT = ?3" +
            " AND HIDDEN_FROM_JURY = FALSE")
    Page<PhotoCollection> findVisibleByLikesCountEquals(UUID competitionId, UUID categoryId, int i, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION" +
            " JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID" +
            " WHERE COMPETITION_RECORD.CONTEST_ID = ?1 AND COMPETITION_RECORD.CATEGORY_ID = ?2 AND" +
            " HIDDEN_FROM_JURY = FALSE")
    Page<PhotoCollection> findVisibleCollectionsByContestIdAndCategoryId(UUID contestId, UUID categoryId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION" +
            " JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID" +
            " WHERE COMPETITION_RECORD.CONTEST_ID = ?1 AND COMPETITION_RECORD.CATEGORY_ID = ?2"
    )
    List<PhotoCollection> findCollectionsByContestIdAndCategoryId(UUID contestId, UUID categoryId);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION" +
            " JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID" +
            " WHERE COMPETITION_RECORD.CONTEST_ID = ?1 AND COMPETITION_RECORD.USER_ID = ?2"
    )
    List<PhotoCollection> findCollectionsByContestIdAndUserId(UUID contestId, UUID userId);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION" +
            " JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID" +
            " WHERE COMPETITION_RECORD.CONTEST_ID = ?1"
    )
    List<PhotoCollection> findCollectionsByContestId(UUID contestId);

    List<PhotoCollection> findCollectionsByCompetitionRecordId(UUID id);

    void deleteByCompetitionRecordId(UUID id);
}
