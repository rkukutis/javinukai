package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.config.security.UserRole;
import lt.javinukai.javinukai.dto.request.collection.CollectionUpdateRequest;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.Photo;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.*;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;
import lt.javinukai.javinukai.utility.ProcessedImage;
import lt.javinukai.javinukai.wrapper.PhotoCollectionWrapper;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoService {
    private final CompetitionRecordService recordService;
    private final PhotoCollectionRepository photoCollectionRepository;
    private final LimitCheckingService limitCheckingService;

    @Value("${app.scheme}")
    private String scheme;

    @Value("${app.host}")
    private String host;

    public PhotoCollection createPhoto(MultipartFile[] images, String description, String title, UUID contestId,
                                       UUID categoryId, User participant) throws IOException {

        log.info("Received {} photos(s) from user {} for contest {} category {}",
                images.length, participant.getEmail(), contestId, categoryId);
        CompetitionRecord competitionRecord = recordService
                .retrieveUserCompetitionRecord(categoryId, contestId, participant.getId());

        ZonedDateTime endDate = competitionRecord.getContest().getEndDate();
        if (endDate.equals(ZonedDateTime.now()) || endDate.isBefore(ZonedDateTime.now())) {
            throw new ContestExpiredException("You can not submit any new photos to a contest that has ended");
        }

        // CONTEST AND CATEGORY LIMITS
        if (!limitCheckingService.checkContestLimit(competitionRecord)) {
            throw new UploadLimitException("Competition entry limit reached");
        }
        if(!limitCheckingService.checkCategoryLimit(competitionRecord)) {
            throw new UploadLimitException("Category entry limit reached");
        }
        // ACCOUNT LIMITS
        // check if user's total entry number does not exceed user maxTotal limit
        if (!limitCheckingService.checkUserContestLimit(competitionRecord)) {
            throw new UploadLimitException("Account competition entry limit reached");
        }
        // check if user does not exceed his own category limit
        if(!limitCheckingService.checkUserCategoryLimit(competitionRecord)) {
            throw new UploadLimitException("Account category entry limit reached");
        }

        // we have to check image validity before working with them because we cant roll back file IO
         validateImages(images);

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


    public byte[] getImageById(UUID imageId, ImageSize size) {
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


    private void validateImages(MultipartFile[] images) {
        for (MultipartFile image : images) {
            if (!image.getContentType().equals("image/jpeg")) {
                throw new ImageValidationException(image.getOriginalFilename());
            }
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

    public String generatedContestThumbnail(UUID contestId, MultipartFile file) {
        if (contestId == null || file == null) {
            throw new RuntimeException("Contest Id or image file must not be null");
        }
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            throw new ImageValidationException("Thumbnail must be an image file");
        }
        try {
            ProcessedImage processedImage = new ProcessedImage(file);
            byte[] imageBytes = processedImage.getResizedImage(ImageSize.SMALL);
            String fileName = contestId + ".jpg";
            Path storagePath = Path.of("src/main/resources/thumbnails", fileName);
            if (Files.exists(storagePath)) Files.delete(storagePath);
            Files.write(storagePath, imageBytes);
            return new URIBuilder()
                    .setScheme(scheme)
                    .setHost(host)
                    .setPathSegments("api", "v1", "contests", contestId.toString(), "thumbnail")
                    .toString();
        } catch (IOException exception) {
            throw new ImageProcessingException("Could not process new contest thumbnail");
        }
    }

    public byte[] getThumbnailBytes(UUID contestId) {
        String filename = contestId.toString() + ".jpg";
        Path storagePath = Path.of("src/main/resources/thumbnails", filename);
        try {
           return Files.readAllBytes(storagePath);
        } catch (IOException exception) {
            throw new NoImagesException("Thumbnail for contest " + contestId + " not found");
        }
    }

    public PhotoCollection updateCollectionInfo(UUID photoCollectionId, CollectionUpdateRequest updatedInfo) {
        PhotoCollection collection = photoCollectionRepository.findById(photoCollectionId)
                .orElseThrow(()-> new EntityNotFoundException("Photo collection with id " +
                        photoCollectionId + " not found"));
        collection.setName(updatedInfo.getNewName());
        collection.setDescription(updatedInfo.getNewDescription());
        return photoCollectionRepository.save(collection);
    }

    private PhotoCollectionWrapper wrapCollectionWithUserRating(PhotoCollection collection, User requestingUser) {
        return PhotoCollectionWrapper.builder()
                .collection(collection)
                .isLiked(collection.getJuryLikes().contains(requestingUser))
                .build();
    }

    public Page<PhotoCollectionWrapper> getContestCategoryCollections(Pageable pageable, User requestingUser,
                                                                      String display, UUID contestId, UUID categoryId) {
        Page<PhotoCollection> photoCollectionPage = photoCollectionRepository.findVisibleCollectionsByContestIdAndCategoryId(contestId, categoryId, pageable);
        if (display.equals("liked")) {
            photoCollectionPage = photoCollectionRepository.findVisibleByLikesCountGreaterThan(contestId, categoryId, 0, pageable);
        } else if (display.equals("unliked")) {
            photoCollectionPage = photoCollectionRepository.findVisibleByLikesCountEquals(contestId, categoryId, 0, pageable);
        }
        return photoCollectionPage.map(photoCollection -> wrapCollectionWithUserRating(photoCollection, requestingUser));
    }

}
