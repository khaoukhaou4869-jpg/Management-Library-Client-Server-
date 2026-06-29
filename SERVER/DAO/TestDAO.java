package DAO;
import model.Book;

import java.util.List;

public class TestDAO {
    public static void main(String[] args) {
        BookDAO dao = new BookDAO();

        // 🔹 Test getAllBooks
        List<Book> books = dao.getAllBooks();
        for (Book b : books) {
            System.out.println(b);
        }

        // 🔹 Test insert
        dao.insertBook(new Book("Test Book", "Tester", true));

        // 🔹 Test update
        dao.updateAvailability(1, false);
    }
}
