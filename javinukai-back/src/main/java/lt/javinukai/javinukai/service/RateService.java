package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateService {
    private final UserService userService;
    private final PhotoCollectionRepository photoCollectionRepository;

    public void rateLike(UUID juryId, UUID collectionId) {
        User jury = userService.getUser(juryId);
        PhotoCollection collection = photoCollectionRepository.getReferenceById(collectionId);
        collection.addLike(jury);
        photoCollectionRepository.save(collection);
    }

    public void undoLike(UUID juryId, UUID collectionId) {
        User jury = userService.getUser(juryId);
        PhotoCollection collection = photoCollectionRepository.getReferenceById(collectionId);
        collection.removeLike(jury);
        photoCollectionRepository.save(collection);
    }

    public List<User> findJurys(UUID collectionId) {

        List<User> n = photoCollectionRepository.findAllJuryByCollectionId(collectionId);
        return n;
    }

    public List<PhotoCollection> findCollections(UUID juryId) {

        List<PhotoCollection> n = photoCollectionRepository.findAllCollectionsByJuryId(juryId);
        return n;
    }

    public boolean pairExists(UUID userId, UUID collectionId) {
        PhotoCollection collection = photoCollectionRepository.findByUserIdAndPhotoCollectionId(userId, collectionId);
        if (collection == null) {
            return false;
        }
        return true;
    }

    public List<User> findAllWhoLiked() {

        List<User> n = photoCollectionRepository.findAllJury();
        return n;
    }

    public void deleteAllLikes() {
        List<PhotoCollection> allLikedCollections = photoCollectionRepository.findAllLikedCollections();
        allLikedCollections.forEach(PhotoCollection::removeLikesFromCollection);
        photoCollectionRepository.saveAll(allLikedCollections);
        }
}
