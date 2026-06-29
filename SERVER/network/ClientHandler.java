package network;

import DAO.BookDAO;
import model.Book;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("📡 Handler started");
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                )
        ) {
            String request;

            while ((request = in.readLine()) != null) {
                System.out.println("📩 Received: " + request);

                if (request.equals("GET_BOOKS")) {

                    List<Book> books = BookDAO.getAllBooks();
                    String response = buildBookListResponse(books);
                    out.println(response);

                }else if (request.startsWith("BORROW")) {

                    String[] parts = request.split("\\|");
                    int id = Integer.parseInt(parts[1]);


                    boolean isAvailable = BookDAO.isBookAvailable(id);

                    if (!isAvailable) {
                        out.println("NOT_AVAILABLE");
                    } else {
                        boolean success = BookDAO.updateAvailability(id, false);

                        if (success) {
                            out.println("OK");
                        } else {
                            out.println("ERROR");
                        }
                    }

                }else if (request.startsWith("RETURN")) {

                    String[] parts = request.split("\\|");
                    int id = Integer.parseInt(parts[1]);

                    // 🔹 Check current state
                    boolean isAvailable = BookDAO.isBookAvailable(id);

                    if (isAvailable) {
                        out.println("ALREADY_RETURNED");
                    } else {
                        boolean success = BookDAO.updateAvailability(id, true);

                        if (success) {
                            out.println("OK");
                        } else {
                            out.println("ERROR");
                        }
                    }
                }else if (request.startsWith("ADD")) {

                    // Format: ADD|title|author
                    String[] parts = request.split("\\|");

                    String title = parts[1];
                    String author = parts[2];

                    Book book = new Book(title, author, true); // available by default
                    boolean success = BookDAO.insertBook(book);

                    if (success) {
                        out.println("OK");
                    } else {
                        out.println("ERROR");
                    }
                }else if (request.startsWith("DELETE")) {

                    String[] parts = request.split("\\|");
                    int id = Integer.parseInt(parts[1]);

                    boolean success = BookDAO.deleteBook(id);

                    if (success) {
                        out.println("OK");
                    } else {
                        out.println("ERROR");
                    }
                }
                else {
                    out.println("ERROR|Unknown command");
                }

            }
        } catch (IOException e) {
            System.out.println("❌ Client disconnected.");
        }
    }

    private String buildBookListResponse(List<Book> books) {
        StringBuilder sb = new StringBuilder("BOOK_LIST|");

        for (Book b : books) {
            sb.append(b.getId()).append(",")
                    .append(b.getTitle()).append(",")
                    .append(b.getAuthor()).append(",")
                    .append(b.isAvailable())
                    .append(";");
        }

        return sb.toString();
    }
}