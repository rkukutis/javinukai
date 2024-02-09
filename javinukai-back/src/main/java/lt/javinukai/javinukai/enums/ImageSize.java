package lt.javinukai.javinukai.enums;

public enum ImageSize {

    // image aspect ratio is assumed to be 3:2

    FULL("/full-size", "full", 9999),
    MIDDLE("/mid-size","middle", 1080),
    SMALL("/small-size", "small", 480),
    THUMBNAIL("/thumbnail-size", "thumbnail", 200);

    public final String localStoragePath;
    public final String value;
    public final int height;

    private ImageSize(String localStoragePath, String value, int height){
        this.localStoragePath = localStoragePath;
        this.value = value;
        this.height = height;}
}
