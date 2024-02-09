package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.ImageProcessingException;
import lt.javinukai.javinukai.exception.NoImagesException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageStorageService {
    private final ContestService contestService;

    public void createImage(MultipartFile[] images, String description, UUID categoryID, UserDetails userDetails)
            throws IOException {

        // Get photo author from userDetails
        User author = (User) userDetails;

        log.info("Received {} photos(s) from user {} for category {}",
                images.length, author.getEmail(), categoryID);

        // we have to check image validity before working with them because we cant roll back file IO
        checkImageSizes(images);

        //  each image will be processed into 4 sizes
        for (MultipartFile image : images) {
            UUID uuid = UUID.randomUUID();
            // Image entity UUID will be set manually
            String imageName = uuid + ".jpg";
            ProcessedImage processedImage = new ProcessedImage(image);
            Map<String, List<String>> pathsAndUrls = new HashMap<>();
            for (ImageSize size : ImageSize.values()) {
                String localPath = storeInFileSystem(processedImage.getResizedImage(size),
                        imageName, size);
                String url = new URIBuilder()
                        .setScheme("http")
                        .setHost("localhost:8080")
                        .setPathSegments("api", "v1", "images", imageName)
                        .setParameter("size", size.value).toString();
                pathsAndUrls.put(size.value, List.of(localPath, url));
            }

                System.out.println(pathsAndUrls);

                // Create a new Photo Entity here with UUID, description, category,
                // author(user) and the 4 urls in constructor method
        }
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

    // maybe we will need to check aspect ratio (3:2) in the future
    private void checkImageAspectRatios(MultipartFile[] images) {
        for (MultipartFile image : images) {
            try (InputStream is = new ByteArrayInputStream(image.getBytes())) {
                BufferedImage img = ImageIO.read(is);
                double aspectRatio = img.getHeight() > img.getWidth() ?
                            (double) img.getWidth() / img.getHeight() :
                            (double) img.getHeight() / img.getWidth();
                if (aspectRatio < 0.6664 || aspectRatio > 0.6668) {
                    throw new ImageProcessingException("Incorrect image " +
                            image.getOriginalFilename() + " aspect ratio: " + aspectRatio);
                }
            } catch (IOException exception) {
                throw new ImageProcessingException("Could not get image " +
                        image.getOriginalFilename() + " input stream");
            }
        }
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
