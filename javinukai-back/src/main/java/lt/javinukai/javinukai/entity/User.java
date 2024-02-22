package lt.javinukai.javinukai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lt.javinukai.javinukai.config.security.UserRole;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.ZonedDateTime;
import java.util.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Setter
    private String name;
    @Setter
    @JsonIgnore
    private String password;
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
    private Integer maxTotal;
    @Setter
    private Integer maxSinglePhotos;
    @Setter
    private Integer maxCollections;
    @Setter
    private Boolean isEnabled;
    @Setter
    private Boolean isNonLocked;

    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<UserToken> tokens;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = CascadeType.MERGE)
    private List<Photo> images;

    @CreatedDate
    private ZonedDateTime createdAt;
    @LastModifiedDate
    private ZonedDateTime modifiedAt;

    @Override
    @JsonIgnore
    public Set<? extends GrantedAuthority> getAuthorities() {
        HashSet<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
    }

    public void addToken(UserToken token) {
        if (this.tokens == null) this.tokens = new HashSet<>();
        this.tokens.add(token);
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isEnabled;
    }
    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = ZonedDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(uuid, user.uuid) && Objects.equals(name, user.name)
                && Objects.equals(password, user.password)
                && Objects.equals(surname, user.surname)
                && Objects.equals(email, user.email)
                && Objects.equals(birthYear, user.birthYear)
                && Objects.equals(phoneNumber, user.phoneNumber)
                && Objects.equals(isFreelance, user.isFreelance)
                && Objects.equals(institution, user.institution)
                && Objects.equals(maxSinglePhotos, user.maxSinglePhotos)
                && Objects.equals(maxCollections, user.maxCollections)
                && Objects.equals(isEnabled, user.isEnabled)
                && Objects.equals(isNonLocked, user.isNonLocked)
                && role == user.role && Objects.equals(tokens, user.tokens)
                && Objects.equals(createdAt, user.createdAt)
                && Objects.equals(modifiedAt, user.modifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, password, surname, email, birthYear, phoneNumber, isFreelance,
                institution, maxSinglePhotos, maxCollections, isEnabled,
                isNonLocked, role, tokens, createdAt, modifiedAt);
    }
}
