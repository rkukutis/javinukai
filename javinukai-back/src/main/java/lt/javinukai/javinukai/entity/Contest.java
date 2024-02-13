package lt.javinukai.javinukai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "contests")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "description")
    private String description;

    // laikinai bus List<String>, po to keisis į List<Category>
    @Setter
    @Column(name = "categories")
    private List<String> category;

    @Setter
    @Column(name = "total_submissions")
    private long totalSubmissions;

    @Setter
    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Setter
    @Column(name = "end_date")
    private ZonedDateTime endDate;

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
