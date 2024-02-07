package lt.javinukai.javinukai.service;

import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.ImageProcessingException;
import lt.javinukai.javinukai.exception.NoImagesException;
import lt.javinukai.javinukai.utility.ProcessedImage;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageService {
    private final ContestService contestService;


    public void createImage(MultipartFile[] images, String description, UUID categoryID, UserDetails userDetails) {

        // Get photo author from userDetails
        User author = (User) userDetails;
        UUID uuid = UUID.randomUUID();
        // Image entity UUID will be set manually
        String imageName = uuid + ".jpg";

        //  each image will be processed into 4 sizes
        for (MultipartFile image : images) {
            try{
            ProcessedImage processedImage = new ProcessedImage(image);
                String pathFullSize = storeInFileSystem(processedImage.getFullSizedImage(),
                    imageName, ImageSize.FULL);
                String pathMidSize = storeInFileSystem(processedImage.getResizedImage(1080),
                    imageName, ImageSize.MIDDLE);
                String pathSmallSize = storeInFileSystem(processedImage.getResizedImage(480),
                    imageName, ImageSize.SMALL);
                String pathThumbnailSize = storeInFileSystem(processedImage.getResizedImage(200),
                    imageName, ImageSize.THUMBNAIL);

                URIBuilder builder = new URIBuilder()
                        .setScheme("http")
                        .setHost("localhost:8080")
                        .setPathSegments("api", "v1", "images", imageName);

                String uriFullSize = builder.setParameter("size", "full").toString();
                String uriMidSize = builder.setParameter("size", "middle").toString();
                String uriSmallSize = builder.setParameter("size", "small").toString();
                String uriThumbnailSize = builder.setParameter("size", "thumbnail").toString();

                // Create a new Photo Entity here with UUID, description, category,
                // author(user) and the 4 urls in constructor method

                System.out.println(pathFullSize);
                System.out.println(pathMidSize);
                System.out.println(pathSmallSize);
                System.out.println(pathThumbnailSize);

                System.out.println(uriFullSize);
                System.out.println(uriMidSize);
                System.out.println(uriSmallSize);
                System.out.println(uriThumbnailSize);



            // Create a new photo entity here that has 4 fields
            } catch (IOException exception) {
                throw new ImageProcessingException("Could not process provided images");
            }
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
        Path storagePathParent = Path.of(root.toString(), size.localStoragePath);
        if (!Files.exists(storagePathParent)) Files.createDirectory(storagePathParent);
        Path storagePath = Path.of(storagePathParent.toString(),imageName);
        Files.write(storagePath, imageBytes);
        return storagePath.toString();
    }

}
