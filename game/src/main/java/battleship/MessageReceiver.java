package battleship;

import battleship.communication.Message;
import battleship.communication.socket.Connection;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runnable que escucha mensajes entrantes en un hilo separado
 */
public class MessageReceiver implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(MessageReceiver.class.getName());
    private Connection connection;
    private MultiplayerGameController gameController;

    public MessageReceiver(Connection connection, MultiplayerGameController gameController) {
        this.connection = connection;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        try {
            while (connection.isConnected()) {
                String raw = connection.getInputStream().readLine();
                if (raw != null) {
                    Message msg = new Message(raw);
                    LOGGER.log(Level.INFO, "Mensaje recibido: {0}", msg.getType());
                    gameController.handleIncomingMessage(msg);
                } else {
                    break; // Conexión cerrada
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en receptor de mensajes: " + e.getMessage(), e);
        }
    }
}