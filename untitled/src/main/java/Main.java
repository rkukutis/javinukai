import java.util.*;

public class Main {

    public static void main(String[] args) {

        genericsTest();
        String string = "bala";
        stringsTest(string);
    }


    public static void stringsTest(String string) {
        int length = string.length();

        Map<Character, Integer> map = new HashMap<>();

        System.out.printf("length -> %d%n", length );

        for (int i = 0; i < string.length(); i++) {
            System.out.printf("string -> %s%n", string.charAt(i));
            map.put(string.charAt(i), map.getOrDefault(string.charAt(i), 0) + 1);
        }

        System.out.println(map);
    }

    public static void genericsTest() {
        Book book = new Book("pupsiks", "jane doe", 777, "fj");
        System.out.println(book);

        Storage<Book> storage = new Storage<>();

        storage.addToStorage(book);
        storage.addToStorage(book);
        System.out.println(storage.displayStorageInventory());
    }

}

class Storage <T> {

    private final List<T> storage;


    public Storage() {
        storage = new ArrayList<>();
    }

    public void addToStorage(T t) {
        storage.add(t);
    }

    public List<T> displayStorageInventory() {
        return  storage;
    }
}

class Book {

    private String name;
    private String author;
    private long pageCount;
    private String isbn;

    public Book() {
        this("book name", "book author", 0, "xxx");
    }
    public Book(String name, String author, long pageCount, String isbn) {
        this.name = name;
        this.author = author;
        this.pageCount = pageCount;
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return String.format("book name -> %s, author -> %s, page count -> %d, isbn -> %s",
                this.name, this.author, this.pageCount, this.isbn);
    }
}
