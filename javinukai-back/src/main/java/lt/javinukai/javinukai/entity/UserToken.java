package lt.javinukai.javinukai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private UUID uuid;

    private String tokenValue;

    @Transient
    private int validMinutes;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    @CreatedDate
    private ZonedDateTime createdAt;

    private ZonedDateTime expiresAt;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_uuid")
    private User user;

    @PrePersist
    protected void prePersist() {
        ZonedDateTime now = ZonedDateTime.now();
        this.createdAt = now;
        this.expiresAt = now.plusMinutes(validMinutes);
    }
}
