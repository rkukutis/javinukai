package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PhotoCollectionRepository extends JpaRepository<PhotoCollection, UUID> {
    @Query("SELECT l FROM PhotoCollection p JOIN p.juryLikes l WHERE p.id = ?1")
    List<User> findAllJuryByCollectionId(UUID collectionId);

    @Query("SELECT l FROM PhotoCollection p JOIN p.juryLikes l")
    List<User> findAllJury();

    @Query("SELECT p FROM PhotoCollection p JOIN p.juryLikes l WHERE l.id = ?1")
    List<PhotoCollection> findLikedCollectionsByJuryId(UUID juryId);

    @Query("SELECT l FROM User u JOIN u.likedCollections l")
    List<PhotoCollection> findAllLikedCollections();

    @Query("SELECT p FROM PhotoCollection p JOIN p.juryLikes l WHERE l.id = ?1 AND p.id = ?2")
    PhotoCollection findByUserIdAndPhotoCollectionId(UUID juryId, UUID photoCollectionId);

    @Query("SELECT COUNT(l) FROM PhotoCollection p JOIN p.juryLikes l WHERE p.id = ?1")
    int countLikes(UUID photoCollectionId);

    @Query("SELECT p FROM PhotoCollection p WHERE p.likesCount = 0")
    List<PhotoCollection> findCollectionsWithNoLikes ();

    @Query("SELECT p FROM PhotoCollection p JOIN p.competitionRecord c WHERE c.contest = ?1 AND p.likesCount = 0")
    List<PhotoCollection> findCollectionsWithNoLikesInContest (Contest contest);

    @Query("SELECT p FROM PhotoCollection p JOIN p.competitionRecord c WHERE c.contest = ?1 AND p.likesCount > 0")
    List<PhotoCollection> findCollectionsWithLikesInContest (Contest contest);

    @Query("SELECT p FROM PhotoCollection p JOIN p.competitionRecord c WHERE c.contest = ?1")
    List<PhotoCollection> findCollectionsInContest (Contest contest);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID WHERE COMPETITION_RECORD.CONTEST_ID = ?1 AND COMPETITION_RECORD.CATEGORY_ID = ?2 AND PHOTO_COLLECTION.LIKES_COUNT > ?3")
    Page<PhotoCollection> findByLikesCountGreaterThan(UUID competitionId, UUID categoryId, int i, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID WHERE COMPETITION_RECORD.CONTEST_ID = ?1 AND COMPETITION_RECORD.CATEGORY_ID = ?2 AND PHOTO_COLLECTION.LIKES_COUNT = ?3")
    Page<PhotoCollection> findByLikesCountEquals(UUID competitionId, UUID categoryId, int i, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT PHOTO_COLLECTION.* FROM PHOTO_COLLECTION JOIN COMPETITION_RECORD ON PHOTO_COLLECTION.COMPETITION_RECORD_ID=COMPETITION_RECORD.ID WHERE COMPETITION_RECORD.CONTEST_ID = ?1 AND COMPETITION_RECORD.CATEGORY_ID = ?2")
    Page<PhotoCollection> findCollectionsByContestIdAndCategoryId(UUID contestId, UUID categoryId, Pageable pageable);
}
