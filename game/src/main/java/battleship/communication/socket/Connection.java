package battleship.communication.socket;

import java.io.BufferedReader;

public interface Connection {
    void start();
    void sendMessage(String msg);
    boolean isConnected();
    BufferedReader getInputStream();
}
