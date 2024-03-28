package lt.javinukai.javinukai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.javinukai.javinukai.enums.TokenType;
import org.springframework.data.annotation.CreatedDate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@NoArgsConstructor
@Getter
public class UserToken {

    public UserToken(String value, TokenType type, User user, int validMinutes) {
        this.tokenValue = value;
        this.type = type;
        this.user = user;
        this.validMinutes = validMinutes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    private String tokenValue;

    @Transient
    private int validMinutes;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    @CreatedDate
    private ZonedDateTime createdAt;

    @Setter
    private ZonedDateTime expiresAt;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void prePersist() {
        ZonedDateTime now = ZonedDateTime.now();
        this.createdAt = now;
        this.expiresAt = now.plusMinutes(validMinutes);
    }
}
