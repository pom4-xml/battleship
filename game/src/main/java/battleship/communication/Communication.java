package battleship.communication;

import battleship.communication.socket.Client;
import battleship.communication.socket.NetworkConnection;
import battleship.communication.socket.Server;

public class Communication {
    public Communication() {

    }

    public NetworkConnection establishConnection() {
        NetworkConnection connection = new Client("127.0.0.1", 5000);
        connection.start();
        if (!connection.isConnected()) {
            connection = new Server(5000);
            connection.start();
        }
        return connection;
    }
}
