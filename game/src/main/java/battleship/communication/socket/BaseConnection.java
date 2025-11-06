package battleship.communication.socket;

import java.io.*;
import java.net.*;

public abstract class BaseConnection extends NetworkConnection {
    
    @Override
    public final void start() {
        try {
            Socket socket = connect();
            if (socket != null) {
                System.out.println("✓ Connected!");
                this.setConnected(true);
                setupStreams(socket);
                
                // Mantener socket abierto
                while (isConnected()) {
                    Thread.sleep(100);
                }
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            this.setConnected(false);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.setConnected(false);
        }
    }

    /**
     * Cada subclase implementa cómo conectarse
     */
    protected abstract Socket connect() throws IOException;
}