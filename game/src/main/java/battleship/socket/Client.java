package battleship.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private String hostname;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connected to server at " + hostname + ":" + port);
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Thread para recibir mensajes
            Thread receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();
            
            // Thread principal para enviar mensajes
            sendMessages();
            
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            stop();
        }
    }

    private void receiveMessages() {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Server: " + response);
                if ("bye".equalsIgnoreCase(response)) {
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
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client c = new Client("127.0.0.1", 5000);
        c.start();
    }
}