package battleship.communication.socket;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server extends NetworkConnection {
    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ExecutorService executor;

    public Server(int port) {
        super();
        this.port = port;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            System.out.println("Waiting for client connection...");
            
            // Aceptar cliente en un hilo separado
            executor.submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    System.out.println("Client connected!");
                    this.setConnected(true);
                    setupStreams(clientSocket);
                    startCommunication();
                } catch (IOException e) {
                    System.err.println("Client connection error: " + e.getMessage());
                }
            });
            
            // Mantener el servidor ejecutándose indefinidamente
            Thread.currentThread().join();
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }

    @Override
    protected String getRemoteLabel() {
        return "Client";
    }
}