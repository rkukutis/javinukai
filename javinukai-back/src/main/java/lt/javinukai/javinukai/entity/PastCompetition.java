package lt.javinukai.javinukai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "past_competition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PastCompetition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column(name="contest_id")
    private UUID contestID;

    @Column(name="contest_name")
    private String contestName;

    @Column(name="contest_description", columnDefinition = "TEXT")
    private String contestDescription;

    @Column(name="category_name")
    private List<String> categories;

    @Column
    private List<String> participants;

    @Column(name="winners")
    private List<String> winners;

    @Setter
    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Setter
    @Column(name = "end_date")
    private ZonedDateTime endDate;
}
