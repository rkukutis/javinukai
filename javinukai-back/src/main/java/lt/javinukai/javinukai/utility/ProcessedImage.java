package lt.javinukai.javinukai.utility;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessedImage {
    private BufferedImage image;

    public ProcessedImage(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        image = ImageIO.read(is);
        is.close();
    }

    public byte[] getResizedImage(int newHeight) throws IOException {
        if (newHeight > image.getHeight()){
            // Do not upscale image
            return getImageBytes(this.image);
        }
        double factor = (double) newHeight / image.getHeight();
        int newWidth = (int)(image.getWidth() * factor);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics graphics = resizedImage.createGraphics();
        graphics.drawImage(image,0,0,newWidth, newHeight, null);
        graphics.dispose();
        return getImageBytes(resizedImage);
    }

    public byte[] getFullSizedImage() throws IOException {
        return getImageBytes(this.image);
    }


    private byte[] getImageBytes(BufferedImage processedImage) throws IOException {
        try (var os = new ByteArrayOutputStream()){
            ImageIO.write(processedImage, "jpg", os);
            return os.toByteArray();
        }
    }
}