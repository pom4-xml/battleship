package battleship.communication.socket;

public interface Connection {
    void start();
    void sendMessage(String msg);
    boolean isConnected();
}
