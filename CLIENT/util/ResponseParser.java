package util;

import model.Book;

import java.util.ArrayList;
import java.util.List;

public class ResponseParser {

    public static List<Book> parseBooks(String response) {
        List<Book> books = new ArrayList<>();


        if (response == null || !response.startsWith("BOOK_LIST|")) {
            return books;
        }


        String data = response.substring("BOOK_LIST|".length());


        String[] bookEntries = data.split(";");

        for (String entry : bookEntries) {
            if (entry.isEmpty()) continue;

            String[] fields = entry.split(",");

            int id = Integer.parseInt(fields[0]);
            String title = fields[1];
            String author = fields[2];
            boolean available = Boolean.parseBoolean(fields[3]);

            books.add(new Book(id, title, author, available));
        }

        return books;
    }
}