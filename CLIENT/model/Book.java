package model;

public class Book {
    private int id;
    private String title;
    private String author;
    private boolean available;

    public static void main(String[] args) {
        Book b = new Book(1, "Clean Code", "Robert Martin", true);
        System.out.println(b);


    }


    public Book() {
    }


    public Book(String title, String author, boolean available) {
        this.title = title;
        this.author = author;
        this.available = available;
    }


    public Book(int id, String title, String author, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


    @Override
    public String toString() {
        return id + "," + title + "," + author + "," + available;
    }
}