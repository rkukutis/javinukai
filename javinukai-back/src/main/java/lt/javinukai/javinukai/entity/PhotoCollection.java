package lt.javinukai.javinukai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String name;
    private String description;
    private UUID category;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_uuid")
    private User author;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "collection", cascade = CascadeType.MERGE)
    private List<Photo> images;

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
