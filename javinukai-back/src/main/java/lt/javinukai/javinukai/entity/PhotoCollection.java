package lt.javinukai.javinukai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.repository.PhotoCollectionRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class PhotoCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Setter
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User author;

    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "collection", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Photo> images;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "competition_record_id", referencedColumnName = "id")
    private CompetitionRecord competitionRecord;

    @Setter
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "jury_id")
    )
    @JsonIgnore
    private List<User> juryLikes;

    @Setter
    @Column(name = "hidden_from_jury")
    private boolean hidden;

    @Setter
    @Column(name = "likes_count")
    private int likesCount;

    public void addLike(User jury) {
        if (juryLikes == null) {
            juryLikes = new ArrayList<>();
        }
        juryLikes.add(jury);
    }

    public void removeLike(User jury) {
        if (juryLikes == null) {
            return;
        }
        juryLikes.remove(jury);
    }
    public void removeAllLikesFromCollection(){
        if (juryLikes == null) {
            return;
        }
        juryLikes.clear();
    }

    public void resetImages() {
        images = null;
    }

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
