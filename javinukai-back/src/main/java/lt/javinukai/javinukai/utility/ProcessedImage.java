package lt.javinukai.javinukai.utility;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.ImageProcessingException;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ProcessedImage {
    private BufferedImage image;
    private String originalName;

    public ProcessedImage(MultipartFile file) throws ImageProcessingException {
        try (InputStream is = file.getInputStream()) {
        this.originalName = file.getOriginalFilename();
        log.info("Started processing image {}", originalName);
        image = ImageIO.read(is);
        } catch (IOException exception) {
            throw new ImageProcessingException("Could not get image input stream");
        }
    }

    public byte[] getResizedImage(ImageSize size) throws ImageProcessingException {
        try {
            // return image unchanged if requested new height is bigger than current image height
            // or if we want the full size images (9999 height is arbitrary)
            if (size.height > image.getHeight() || size.height == 9999) {
                return getImageBytes(this.image);
            }
            int newWidth = 0;
            int newHeight = 0;
            double aspectRatio = (double) image.getWidth() / image.getHeight();
            double factor = (double) size.height / image.getHeight();
            if (image.getWidth() >= image.getHeight()) {
                newWidth = (int) (image.getWidth() * factor);
                newHeight = size.height;
            } else {
                newHeight = (int) (size.height / aspectRatio);
                newWidth = (int) (newHeight * aspectRatio);
            }
            // This is kinda bad
            if (newHeight % 10 == 9) newHeight++;
            if (newWidth % 10 == 9) newWidth++;
            log.info("Resizing image {} from {}X{} to {}X{}", originalName,
                    image.getWidth(), image.getHeight(), newWidth, newHeight);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());
            Graphics graphics = resizedImage.createGraphics();
            graphics.drawImage(image,0,0, newWidth, newHeight, null);
            graphics.dispose();
            return getImageBytes(resizedImage);
        } catch (IOException exception) {
            throw new ImageProcessingException("Could not create resized image");
        }
    }


    private byte[] getImageBytes(BufferedImage processedImage) throws IOException {
        try (var os = new ByteArrayOutputStream()){
            ImageIO.write(processedImage, "jpg", os);
            log.info("Saving resized image {}", originalName);
            return os.toByteArray();
        }
    }
}