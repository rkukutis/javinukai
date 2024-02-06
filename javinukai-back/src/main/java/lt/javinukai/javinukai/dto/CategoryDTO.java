package lt.javinukai.javinukai.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter

public class CategoryDTO {
    private String name;
    private long totalSubmissions;
    private String competitionID;
    private List<String> uploadedPhoto;
}
