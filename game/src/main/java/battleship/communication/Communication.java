package battleship.communication;


import battleship.communication.socket.Client;
import battleship.communication.socket.Connection;
import battleship.communication.socket.Server;

public class Communication {
    public Communication() {
        // Default constructor intentionally left empty — no initialization needed here
    }

    public Connection establishConnection() {
        Connection connection = new Client("127.0.0.1", 5000);
        connection.start();
        if (!connection.isConnected()) {
            connection = new Server(5000);
            connection.start();
        }
        return connection;
    }

    public static void main(String[] args) {
        Communication communication = new Communication();
        communication.establishConnection();
    }
}
