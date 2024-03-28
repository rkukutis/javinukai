package lt.javinukai.javinukai.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.collection.CollectionUpdateRequest;
import lt.javinukai.javinukai.entity.PhotoCollection;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.enums.ImageSize;
import lt.javinukai.javinukai.exception.NoImagesException;
import lt.javinukai.javinukai.service.PhotoService;
import lt.javinukai.javinukai.wrapper.PhotoCollectionWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.validator.constraints.Length;
import java.io.IOException;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;
    @PostMapping(headers = "content-type=multipart/form-data", consumes = "image/jpg")
    public ResponseEntity<PhotoCollection> uploadImages(@RequestParam("image") MultipartFile[] images,
                                                        @RequestParam("title") @Length(max = 100, message = "TITLE_LENGTH_EXCEEDED") String title,
                                                        @RequestParam("description") @NotBlank @Length(max = 1000, message = "DESCRIPTION_LENGTH_EXCEEDED") String description,
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

    // this endpoint is for the jury, mods and admins
    @GetMapping("contest/{contestId}/category/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_JURY', 'ROLE_MODERATOR')")
    public ResponseEntity<Page<PhotoCollectionWrapper>> getContestCategoryCollections(@PathVariable UUID contestId,
                                                                                      @PathVariable UUID categoryId,
                                                                                      @AuthenticationPrincipal User requestingUser,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "25") int limit,
                                                                                      @RequestParam(defaultValue = "all") String display) {
        log.info("Request for retrieving photo collections for contest {} category {}", contestId, categoryId);
        final Pageable pageable = PageRequest.of(page, limit);
        Page<PhotoCollectionWrapper> photoCollectionPage = photoService.getContestCategoryCollections(pageable, requestingUser, display, contestId, categoryId);
        return ResponseEntity.ok().body(photoCollectionPage);
    }

    @DeleteMapping("{photoCollectionId}")
    public ResponseEntity<String> deletePhotoCollection(@PathVariable UUID photoCollectionId,
                                                        @AuthenticationPrincipal User user) {
        photoService.deleteCollectionById(photoCollectionId, user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("collection/{collectionId}")
    public ResponseEntity<PhotoCollection> updatePhotoCollection(@PathVariable UUID collectionId,
                                                                 @RequestBody @Valid CollectionUpdateRequest updatedInfo) {
        PhotoCollection updatedCollection =  photoService.updateCollectionInfo(collectionId, updatedInfo);
       return ResponseEntity.ok().body(updatedCollection);
    }

}
