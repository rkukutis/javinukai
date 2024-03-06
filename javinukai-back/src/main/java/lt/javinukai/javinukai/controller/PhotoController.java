package lt.javinukai.javinukai.controller;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.NoImagesException;
import lt.javinukai.javinukai.service.PhotoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;


    @PostMapping(headers = "content-type=multipart/form-data", consumes = "image/jpg")
    public ResponseEntity<PhotoCollection> uploadImages(@RequestParam("image") MultipartFile[] images,
                                                        @RequestParam("title") @NotBlank String title,
                                                        @RequestParam("description") @NotBlank String description,
                                                        @RequestParam("contestId") @NotNull UUID contestId,
                                                        @RequestParam("categoryId") @NotNull UUID categoryId,
                                                        @AuthenticationPrincipal User participant
    ) throws IOException {
        if (images.length < 1 ) {
            throw new NoImagesException("No jpg images were provided with request");
        }
        return ResponseEntity.ok()
                .body(photoService.createPhoto(images,description,title, contestId, categoryId, participant));
    }

    @GetMapping("{image}")
    public ResponseEntity<byte[]> getImageWithId(@PathVariable String image, @RequestParam("size") String size)
            throws IOException {
        UUID imageId = UUID.fromString(image.split("\\.")[0]);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(photoService.getImageById(imageId, ImageSize.valueOf(size.toUpperCase())));
    }

    @DeleteMapping("{photoCollectionId}")
    public ResponseEntity<String> deletePhotoCollection(@PathVariable UUID photoCollectionId,
                                                        @AuthenticationPrincipal User user) {
        photoService.deleteCollectionById(photoCollectionId, user);
        return ResponseEntity.noContent().build();
    }

}
