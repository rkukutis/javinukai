package lt.javinukai.javinukai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PastCompetitionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column(name="contest_name")
    private String contestName;

    @Column(name="contest_description")
    private String contestDescription;

    @Column(name="category_name")
    private String categoryName;

    @Column(name="category_description")
    private String categoryDescription;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="is_winner")
    private boolean isWinner;
}
