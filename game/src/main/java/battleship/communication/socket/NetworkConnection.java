package battleship.communication.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public abstract class NetworkConnection {
    protected Scanner scanner;
    protected PrintWriter out;
    protected BufferedReader in;
    private boolean isConnected;
    
    public NetworkConnection() {
        this.scanner = new Scanner(System.in);
        isConnected = false;
    }

    public abstract void start();

    protected void receiveMessages() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(getRemoteLabel() + ": " + inputLine);
                if ("bye".equalsIgnoreCase(inputLine)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error receiving messages: " + e.getMessage());
        }
    }

    public void sendMessage(String msg) {
        System.out.println("Type your messages (type 'bye' to exit):");
        String userInput;
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("bye")) {
            out.println(userInput);
        }
        out.println("bye");
    }

    protected void setupStreams(Socket socket) throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    protected void startCommunication() {
        // Thread para recibir mensajes
        Thread receiveThread = new Thread(this::receiveMessages);
        receiveThread.start();
    }

    

    protected abstract String getRemoteLabel();

    public Scanner getScanner() {
        return scanner;
    }
    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}