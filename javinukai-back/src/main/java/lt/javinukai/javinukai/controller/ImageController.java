package lt.javinukai.javinukai.controller;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.NoImagesException;
import lt.javinukai.javinukai.service.ImageStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageStorageService imageStorageService;


    @PostMapping(headers = "content-type=multipart/form-data", consumes = "image/jpg")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("images") MultipartFile[] images,
                                                     @RequestParam("description") @NotBlank String description,
                                                     @RequestParam("categoryId") @NotNull UUID categoryID,
                                                     @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (images.length < 1 ) {
            throw new NoImagesException("No jpg images were provided with request");
        }
        imageStorageService.createImage(images, description, categoryID, userDetails);
        return ResponseEntity.created(URI.create("/images")).build();
    }

    @GetMapping("{image}")
    public ResponseEntity<byte[]> getImageWithId(@PathVariable String image, @RequestParam("size") String size)
            throws IOException {
        UUID imageId = UUID.fromString(image.split("\\.")[0]);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(imageStorageService.getImageById(imageId, ImageSize.valueOf(size.toUpperCase())));
    }

}
