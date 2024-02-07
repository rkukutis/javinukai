package lt.javinukai.javinukai.enums;

public enum ImageSize {

    FULL("/full-size", "full"),
    MIDDLE("/mid-size","middle"),
    SMALL("/small-size", "small"),
    THUMBNAIL("/thumbnail-size", "thumbnail");

    public final String localStoragePath;
    public final String value;

    private ImageSize(String localStoragePath, String value){
        this.localStoragePath = localStoragePath;
        this.value = value;
    }
}
