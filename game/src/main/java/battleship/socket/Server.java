package battleship.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;

    public Server(int port) {
        this.port = port;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            System.out.println("Waiting for client connection...");
            
            clientSocket = serverSocket.accept();
            System.out.println("Client connected!");
            
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            // Thread para recibir mensajes
            Thread receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();
            
            // Thread principal para enviar mensajes
            sendMessages();
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            stop();
        }
    }

    private void receiveMessages() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client: " + inputLine);
                if ("bye".equalsIgnoreCase(inputLine)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error receiving messages: " + e.getMessage());
        }
    }

    private void sendMessages() {
        System.out.println("Type your messages (type 'bye' to exit):");
        String userInput;
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("bye")) {
            out.println(userInput);
        }
        out.println("bye");
    }

    public void stop() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }
}