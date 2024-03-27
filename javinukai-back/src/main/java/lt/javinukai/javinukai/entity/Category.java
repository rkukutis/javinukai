package lt.javinukai.javinukai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lt.javinukai.javinukai.enums.PhotoSubmissionType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Setter
    @Column(name = "max_total_submissions")
    private long maxTotalSubmissions;

    @Setter
    @Column(name = "max_user_submissions")
    private long maxUserSubmissions;

    @Setter
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories",
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})

    @JsonIgnore
    private List<Contest> contests;

    @Setter
    @Column(name = "uploaded_photo")
    private List<String> uploadedPhotos;

    @CreatedDate
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonIgnore
    private List<CompetitionRecord> competitionRecords = new ArrayList<>();

    @Setter
    @Enumerated(EnumType.STRING)
    private PhotoSubmissionType type;

    public void addContest(Contest contest) {
        if (contests == null) {
            contests = new ArrayList<>();
        }
        contests.add(contest);
    }

    public void removeContest(Contest contest) {
        if (contests == null) {
            return;
        }
        contests.remove(contest);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = ZonedDateTime.now();
    }
}