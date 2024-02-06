package lt.javinukai.javinukai.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ContestDTO {
    private String name;
    private String description;
    private List<String> category;
    private long totalSubmissions;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}