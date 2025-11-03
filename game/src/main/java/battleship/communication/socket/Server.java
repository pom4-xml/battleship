package battleship.communication.socket;

import java.io.*;
import java.net.*;

public class Server extends NetworkConnection {
    private int port;

    public Server(int port) {
        super();
        this.port = port;
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            System.out.println("Waiting for client connection...");
            
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected!");
                this.setConnected(true);
                setupStreams(clientSocket);
                startCommunication();
                
            } catch (IOException e) {
                System.err.println("Client connection error: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    @Override
    protected String getRemoteLabel() {
        return "Client";
    }

    public static void main(String[] args) {
        Server server = new Server(5000);
        server.start();
    }
}