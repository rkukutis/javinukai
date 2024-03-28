package lt.javinukai.javinukai.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lt.javinukai.javinukai.entity.Category;

@SuperBuilder
@Getter
public class CategoryWithEntriesResponse extends Category {

    private int totalEntries;
}
