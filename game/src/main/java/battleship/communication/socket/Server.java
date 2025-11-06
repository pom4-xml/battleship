package battleship.communication.socket;

import java.io.*;
import java.net.*;

public class Server extends BaseConnection {
    private int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        super();
        this.port = port;
    }

    @Override
    protected Socket connect() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        System.out.println("Waiting for client connection...");
        
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected!");
        return clientSocket;
    }

    @Override
    public void setConnected(boolean b) {
        super.setConnected(b);
        if (!b && serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing serverSocket: " + e.getMessage());
            }
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