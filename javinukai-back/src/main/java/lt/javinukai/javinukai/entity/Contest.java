package lt.javinukai.javinukai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "contests")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
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

    @Setter
    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Setter
    @Column(name = "end_date")
    private ZonedDateTime endDate;
}
