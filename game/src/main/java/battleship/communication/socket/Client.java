package battleship.communication.socket;

import java.io.*;
import java.net.*;

public class Client extends NetworkConnection {
    private String hostname;
    private int port;
    private Socket socket;

    public Client(String hostname, int port) {
        super();
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void start() {
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connected to server at " + hostname + ":" + port);
            this.setConnected(true);
            setupStreams(socket);
            startCommunication();
            
            // Mantener el socket abierto: esperar indefinidamente
            // El socket se cierra solo cuando la conexión se termina remotamente
            while (isConnected()) {
                Thread.sleep(100);
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    public boolean canConnect(int timeoutMillis) {
        Socket testSocket = null;
        try {
            testSocket = new Socket();
            testSocket.connect(new InetSocketAddress(hostname, port), timeoutMillis);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (testSocket != null) {
                try {
                    testSocket.close();
                } catch (IOException e) {
                    // Ignorar error al cerrar socket de prueba
                }
            }
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
