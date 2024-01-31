package lt.javinukai.javinukai.dto;


import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ContestDTO {
    private String name;
    private String description;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
