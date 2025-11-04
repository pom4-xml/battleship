package battleship.communication.socket;

import java.io.*;
import java.net.*;

public class Client extends NetworkConnection {
    private String hostname;
    private int port;

    public Client(String hostname, int port) {
        super();
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void start() {
        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to server at " + hostname + ":" + port);
            this.setConnected(true);
            setupStreams(socket);
            startCommunication();
            
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    @Override
    protected String getRemoteLabel() {
        return "Server";
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5000);
        client.start();
    }
}
