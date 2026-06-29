package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 1234;

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("✅ Server started on port " + PORT);

            while (true) {
                System.out.println("⏳ Waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("✅ Client connected!");
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start(); // VERY IMPORTANT (thread)


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

