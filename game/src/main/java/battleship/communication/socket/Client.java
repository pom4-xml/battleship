package battleship.communication.socket;

import java.io.*;
import java.net.*;

public class Client extends BaseConnection {
    private String hostname;
    private int port;

    public Client(String hostname, int port) {
        super();
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    protected Socket connect() throws IOException {
        System.out.println("Connecting to server at " + hostname + ":" + port);
        return new Socket(hostname, port);
    }

    public boolean canConnect(int timeoutMillis) {
        try (Socket testSocket = new Socket()) {
            testSocket.connect(new InetSocketAddress(hostname, port), timeoutMillis);
            return true;
        } catch (IOException e) {
            return false;
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
