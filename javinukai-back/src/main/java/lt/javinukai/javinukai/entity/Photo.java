package lt.javinukai.javinukai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photos")
public class Photo {

    @Id
    @Column(name = "id")
    // This is NOT auto-generated
    private UUID id;

    private String localPathFull;
    private String localPathMiddle;
    private String localPathSmall;
    private String localPathThumbnail;
    private String urlFull;
    private String urlMiddle;
    private String urlSmall;
    private String urlThumbnail;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "collection_id")
    private PhotoCollection collection;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private User author;

    private ZonedDateTime createdAt;
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
