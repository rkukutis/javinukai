package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Photo;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.ImageDeleteException;
import lt.javinukai.javinukai.exception.ImageProcessingException;
import lt.javinukai.javinukai.exception.ImageValidationException;
import lt.javinukai.javinukai.exception.NoImagesException;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;
import lt.javinukai.javinukai.utility.ProcessedImage;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoService {
    private final CompetitionRecordService recordService;
    private final PhotoCollectionRepository photoCollectionRepository;

    @Value("${app.scheme}")
    private String scheme;

    @Value("${app.host}")
    private String host;

    public PhotoCollection createPhoto(MultipartFile[] images, String description, String title, UUID contestId,
                                       UUID categoryId, User participant) throws IOException {
        // we have to check image validity before working with them because we cant roll back file IO
         checkImageSizes(images);

        log.info("Received {} photos(s) from user {} for contest {} category {}",
                images.length, participant.getEmail(), contestId, categoryId);
        CompetitionRecord competitionRecord = recordService
                .retrieveUserCompetitionRecord(categoryId, contestId, participant.getId());
        PhotoCollection collection = photoCollectionRepository.save(PhotoCollection.builder()
                .name(title)
                .description(description)
                .author(participant)
                .competitionRecord(competitionRecord)
                .images(new ArrayList<>())
                .build()
        );
        //  each image will be processed into 4 sizes
        for (MultipartFile image : images) {
            UUID uuid = UUID.randomUUID();
            // Image entity UUID will be set manually
            String imageName = uuid + ".jpg";
            Map<ImageSize, List<String>> pathsAndUrls = getImagePathsAndUrls(image, imageName);
            var contestantImage = Photo.builder()
                    .id(uuid)
                    .localPathFull(imageName)
                    .author(participant)
                    .collection(collection)
                    .localPathFull(pathsAndUrls.get(ImageSize.FULL).get(0))
                    .localPathMiddle(pathsAndUrls.get(ImageSize.MIDDLE).get(0))
                    .localPathSmall(pathsAndUrls.get(ImageSize.SMALL).get(0))
                    .localPathThumbnail(pathsAndUrls.get(ImageSize.THUMBNAIL).get(0))
                    .urlFull(pathsAndUrls.get(ImageSize.FULL).get(1))
                    .urlMiddle(pathsAndUrls.get(ImageSize.MIDDLE).get(1))
                    .urlSmall(pathsAndUrls.get(ImageSize.SMALL).get(1))
                    .urlThumbnail(pathsAndUrls.get(ImageSize.THUMBNAIL).get(1))
                    .build();
            collection.getImages().add(contestantImage);
        }
        return photoCollectionRepository.save(collection);
    }

    private Map<ImageSize, List<String>> getImagePathsAndUrls(MultipartFile image, String name) throws IOException {
        ProcessedImage processedImage = new ProcessedImage(image);
        Map<ImageSize, List<String>> pathsAndUrls = new HashMap<>();
        for (ImageSize size : ImageSize.values()) {
            String localPath = storeInFileSystem(processedImage.getResizedImage(size),
                    name, size);
            String url = new URIBuilder()
                    .setScheme(scheme)
                    .setHost(host)
                    .setPathSegments("api", "v1", "images", name)
                    .setParameter("size", size.value).toString();
            pathsAndUrls.put(size, List.of(localPath, url));
        }
        return  pathsAndUrls;
    }


    public byte[] getImageById(UUID imageId, ImageSize size) throws IOException {
        try {
        Path imagePath = Path.of("src/main/resources/images",size.localStoragePath, imageId + ".jpg");
        return Files.readAllBytes(imagePath);
        } catch (IOException exception) {
            throw new NoImagesException("No image found with id: " + imageId);
        }
    }


    private String storeInFileSystem(byte[] imageBytes, String imageName, ImageSize size) throws IOException {
        Path root = Path.of("src/main/resources/images");
        if (!Files.exists(root)) Files.createDirectory(root);
        Path storagePathParent = Path.of(root.toString(), size.localStoragePath);
        if (!Files.exists(storagePathParent)) Files.createDirectory(storagePathParent);
        Path storagePath = Path.of(storagePathParent.toString(),imageName);
        Files.write(storagePath, imageBytes);
        return storagePath.toString();
    }


    private void checkImageSizes(MultipartFile[] images) {
        for (MultipartFile image : images) {
            image.getContentType();
            try (InputStream is = new ByteArrayInputStream(image.getBytes())){
            BufferedImage img = ImageIO.read(is);
                if (img.getHeight() > img.getWidth()) { // image is a portrait
                    if (img.getHeight() < 2500 || img.getHeight() > 4000) {
                        throw new ImageValidationException(image.getOriginalFilename());
                    }
                } else {
                    if (img.getWidth() < 2500 || img.getWidth() > 4000) {
                        throw new ImageValidationException(image.getOriginalFilename());
                    }
                }
            } catch (IOException exception) {
                throw new ImageProcessingException("Could not get image " +
                        image.getOriginalFilename() + " input stream");
            }
        }
    }

    @Transactional
    public void deleteCollectionById(UUID photoCollectionId, User requestingUser) {
        PhotoCollection collection = photoCollectionRepository.findById(photoCollectionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Photo collection with id:" + photoCollectionId + " not found")
                );

        if (!collection.getAuthor().getId().equals(requestingUser.getId()) &&
                requestingUser.getRole() != UserRole.ADMIN
                ) {
            throw new BadCredentialsException("Only the author and admin can delete an photo collection/entry");
        }
            collection.getImages().forEach(photo -> {
                try {
                    deleteLocalPhotoById(photo.getId());
                } catch (IOException e) {
                    throw new ImageDeleteException("Could not delete local photo copy");
                }
            });
        photoCollectionRepository.deleteById(photoCollectionId);
    }

    private void deleteLocalPhotoById(UUID photoId) throws IOException {
        for (ImageSize imageSize : ImageSize.values()) {
            Path imagePath = Path.of(
                    "src/main/resources/images",imageSize.localStoragePath, photoId + ".jpg"
            );
            Files.delete(imagePath);
        }
    }


}
