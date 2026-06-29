package network;

import model.Book;
import util.ResponseParser;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class TestClient {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 1234);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                )
        ) {
            out.println("GET_BOOKS");

            String response = in.readLine();
            System.out.println("Raw Response: " + response);

            // 🔹 Parse it
            List<Book> books = ResponseParser.parseBooks(response);

            // 🔹 Print results
            for (Book b : books) {
                System.out.println("Parsed: " + b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}