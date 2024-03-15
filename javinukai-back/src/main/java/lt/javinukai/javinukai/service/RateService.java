package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateService {
    private final UserService userService;
    private final PhotoCollectionRepository photoCollectionRepository;

    public void rateLike (UUID juryId, UUID collectionId){
        User jury = userService.getUser(juryId);
        PhotoCollection collection = photoCollectionRepository.getReferenceById(collectionId);
        collection.addJury(jury);
        photoCollectionRepository.save(collection);
    }

    public void undoLike (UUID juryId, UUID collectionId){
        User jury = userService.getUser(juryId);
        PhotoCollection collection = photoCollectionRepository.getReferenceById(collectionId);
        collection.removeJury(jury);
        photoCollectionRepository.save(collection);
    }

}
