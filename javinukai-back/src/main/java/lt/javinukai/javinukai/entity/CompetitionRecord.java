package lt.javinukai.javinukai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "record")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetitionRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "contest_id")
    private UUID contestID;

    @Setter
    @Column(name = "contest_name")
    private String contestName;

    @Setter
    @Column(name = "user_id")
    private UUID userID;

    @Setter
    @Column(name = "user_name")
    private String userName;


    @Setter
    @Column(name = "max_photos")
    private long maxPhotos;

//    @Setter
//    @Column(name = "max_collections")
//    private long maxCollections;

    @Setter
    @Column
    private List<String> photos;

    public void addPhotos(List<String> photosToAdd, long limit) {
        if (photos == null) {
            photos = new ArrayList<>();
        }

        for (int i = 0; i < photosToAdd.size(); i++) {
            if (i < limit) {
                photos.add(photosToAdd.get(i));
            }
        }
    }

    @CreatedDate
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = ZonedDateTime.now();
    }

}
