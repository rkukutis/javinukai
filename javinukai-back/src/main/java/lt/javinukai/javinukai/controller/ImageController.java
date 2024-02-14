package lt.javinukai.javinukai.controller;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.entity.ContestantImageCollection;
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
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageStorageService imageStorageService;


    @PostMapping(headers = "content-type=multipart/form-data", consumes = "image/jpg")
    public ResponseEntity<ContestantImageCollection> uploadImages(@RequestParam("image") MultipartFile[] images,
                                                                  @RequestParam("title") @NotBlank String title,
                                                                  @RequestParam("description") @NotBlank String description,
                                                                  @RequestParam("categoryId") @NotNull UUID categoryID,
                                                                  @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (images.length < 1 ) {
            throw new NoImagesException("No jpg images were provided with request");
        }
        return ResponseEntity.ok()
                .body(imageStorageService.createImage(images, title, description, categoryID, userDetails));
    }

    @GetMapping("{image}")
    public ResponseEntity<byte[]> getImageWithId(@PathVariable String image, @RequestParam("size") String size)
            throws IOException {
        UUID imageId = UUID.fromString(image.split("\\.")[0]);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(imageStorageService.getImageById(imageId, ImageSize.valueOf(size.toUpperCase())));
    }

}
