package lt.javinukai.javinukai.wrapper;

import lombok.Builder;
import lombok.Getter;
import lt.javinukai.javinukai.entity.PhotoCollection;

@Builder
@Getter
public class PhotoCollectionWrapper {

    private PhotoCollection collection;
    private boolean isLiked;
}
