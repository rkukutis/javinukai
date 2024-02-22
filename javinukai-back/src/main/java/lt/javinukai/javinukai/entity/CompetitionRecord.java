package lt.javinukai.javinukai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lt.javinukai.javinukai.repository.CategoryRepository;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "competition_record")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CompetitionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contest_id", referencedColumnName = "id")
    private Contest contest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    private User user;

    @Setter
    @Column(name = "max_photos")
    private long maxPhotos;

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


//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "id")
//    private UUID id;

//    @Setter
//    @Column(name = "contest_id")
//    private UUID contestID;
//
//    @Setter
//    @Column(name = "contest_name")
//    private String contestName;

//    @Setter
//    @Column(name = "user_id")
//    private UUID userID;
//
//    @Setter
//    @Column(name = "user_name")
//    private String userName;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinColumn(name = "category_id")
//    @JsonIgnore
//    @Setter
//    private Category category;

//    @Setter
//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "record_category",
//            joinColumns = @JoinColumn(name = "record_id"),
//            inverseJoinColumns = @JoinColumn(name = "category_id")
//    )
//    private List<Category> categories;

//    @Setter
//    @Column(name = "category_name")
//    private String categoryName;

//    @Setter
//    @Column(name = "max_photos")
//    private long maxPhotos;

//    @Setter
//    @Column
//    private List<String> photos;


//    public void addPhotos(List<String> photosToAdd, long limit) {
//        if (photos == null) {
//            photos = new ArrayList<>();
//        }
//
//        for (int i = 0; i < photosToAdd.size(); i++) {
//            if (i < limit) {
//                photos.add(photosToAdd.get(i));
//            }
//        }
//    }
//
//    @CreatedDate
//    @Column(name = "created_at")
//    private ZonedDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "modified_at")
//    private ZonedDateTime modifiedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = ZonedDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.modifiedAt = ZonedDateTime.now();
//    }

}
