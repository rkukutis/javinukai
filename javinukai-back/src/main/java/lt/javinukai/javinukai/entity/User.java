package lt.javinukai.javinukai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// this will implement security UserDetails interface
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Setter
    private String name;
    @Setter
    private String surname;
    @Setter
    private String email;
    @Setter
    private Integer birthYear;
    @Setter
    private String phoneNumber;
    @Setter
    private Boolean isFreelance;
    @Setter
    private String institution;
    @Setter
    private Integer maxSinglePhotos;
    @Setter
    private Integer maxCollections;

    @CreatedDate
    private ZonedDateTime createdAt;
    @LastModifiedDate
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
