package lt.javinukai.javinukai.service;

import lt.javinukai.javinukai.entity.Photo;
import lt.javinukai.javinukai.repository.PhotoRepository;

public class PhotoService {
    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }


}
