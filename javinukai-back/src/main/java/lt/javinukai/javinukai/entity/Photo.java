package lt.javinukai.javinukai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "photos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "photo_id")
    private UUID photoId;

    @Setter
    @Column(name = "file_name")
    private String fileName;

    @Setter
    @Column(name = "main")
    private String photoMain;

    @Setter
    @Column(name = "small")
    private String photoSmall;

    @Setter
    @Column(name = "mobile_main")
    private String photoMobile;

    @Setter
    @Column(name = "mobile_small")
    private String photoMobileSmall;

    @Setter
    @Column(name = "photo_description")
    private String photoDescription;

    @Setter
    @Column(name = "collection_id")
    private UUID collectionId;
}
