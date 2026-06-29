package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient2 {
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

            // 🔹 List of books to add
            String[][] books = {
                    {"Clean Code", "Robert Martin"},
                    {"Design Patterns", "GoF"},
                    {"Java Basics", "John Doe"},
                    {"Algorithms", "CLRS"},
                    {"Coding", "Robert Martin"},
                    {"Web Design", "GGG"},
                    {"Java Intermediate", "John Doe"},
                    {"Algebre", "CLRS"}
            };

            for (String[] b : books) {

                String request = "ADD|" + b[0] + "|" + b[1];
                out.println(request);

                String response = in.readLine();
                System.out.println("Added: " + b[0] + " → " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}