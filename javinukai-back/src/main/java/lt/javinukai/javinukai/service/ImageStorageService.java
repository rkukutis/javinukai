package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.ContestantImage;
import lt.javinukai.javinukai.entity.ContestantImageCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.ImageProcessingException;
import lt.javinukai.javinukai.exception.NoImagesException;
import lt.javinukai.javinukai.repository.ContestantImageCollectionRepository;
import lt.javinukai.javinukai.utility.ProcessedImage;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
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
public class ImageStorageService {
    private final ContestService contestService;
    private final ContestantImageCollectionRepository contestantImageCollectionRepository;

    public ContestantImageCollection createImage(MultipartFile[] images, String description,String title,
                                             UUID categoryID, UserDetails userDetails)
            throws IOException {
        User author = (User) userDetails;
        // we have to check image validity before working with them because we cant roll back file IO
        checkImageSizes(images);
        log.info("Received {} photos(s) from user {} for category {}",
                images.length, author.getEmail(), categoryID);
        ContestantImageCollection collection = contestantImageCollectionRepository.save(ContestantImageCollection.builder()
                .name(title)
                .description(description)
                .category(categoryID)
                .author(author)
                .images(new ArrayList<>())
                .build()
        );

        //  each image will be processed into 4 sizes
        for (MultipartFile image : images) {
            UUID uuid = UUID.randomUUID();
            // Image entity UUID will be set manually
            String imageName = uuid + ".jpg";
            ProcessedImage processedImage = new ProcessedImage(image);
            Map<ImageSize, List<String>> pathsAndUrls = new HashMap<>();
            for (ImageSize size : ImageSize.values()) {
                String localPath = storeInFileSystem(processedImage.getResizedImage(size),
                        imageName, size);
                String url = new URIBuilder()
                        .setScheme("http")
                        .setHost("localhost:8080")
                        .setPathSegments("api", "v1", "images", imageName)
                        .setParameter("size", size.value).toString();
                pathsAndUrls.put(size, List.of(localPath, url));
            }
            var contestantImage = ContestantImage.builder()
                    .uuid(uuid)
                    .localPathFull(imageName)
                    .author(author)
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
        return contestantImageCollectionRepository.save(collection);
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
            try (InputStream is = new ByteArrayInputStream(image.getBytes())){
            BufferedImage img = ImageIO.read(is);
                if (img.getHeight() > img.getWidth()) { // image is a portrait
                    if (img.getHeight() < 2500 || img.getHeight() > 4000) {
                        throw new ImageProcessingException("Incorrect image size: " + image.getOriginalFilename());
                    }
                } else {
                    if (img.getWidth() < 2500 || img.getWidth() > 4000) {
                        throw new ImageProcessingException("Incorrect image size: " + image.getOriginalFilename());
                    }
                }
            } catch (IOException exception) {
                throw new ImageProcessingException("Could not get image " +
                        image.getOriginalFilename() + " input stream");
            }
        }
    }

}
