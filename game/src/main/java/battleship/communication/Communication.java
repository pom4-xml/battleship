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
        
        // Arrancar conexión en un hilo separado
        Thread connectionThread = new Thread(connection::start, "connection-thread");
        connectionThread.setDaemon(false);
        connectionThread.start();
        
        // Esperar a que se conecte
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 2000) {
            if (connection.isConnected()) {
                return connection;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        // Si no conectó como cliente, intentar como servidor
        if (!connection.isConnected()) {
            connection = new Server(5000);
            
            Thread serverThread = new Thread(connection::start, "server-thread");
            serverThread.setDaemon(false);
            serverThread.start();
            
            // Esperar a que se conecte
            start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < 120000) {
                if (connection.isConnected()) {
                    return connection;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        return connection;
    }

    public static void main(String[] args) {
        Communication communication = new Communication();
        Connection connection = communication.establishConnection();
        
        if (connection == null || !connection.isConnected()) {
            System.err.println("No se pudo establecer conexión");
            return;
        }

        System.out.println("✓ Conexión establecida\n");

        // Hilo para recibir mensajes
        Thread receiverThread = new Thread(() -> {
            try {
                while (connection.isConnected()) {
                    String message = connection.getInputStream().readLine();
                    if (message == null) break;
                    System.out.println("\n[Recibido] " + message);
                    System.out.print("Tú: ");
                    System.out.flush();
                }
            } catch (Exception e) {
                System.err.println("Error recibiendo: " + e.getMessage());
            }
        }, "receiver-thread");
        receiverThread.setDaemon(false);
        receiverThread.start();

        // Loop para enviar mensajes
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            while (connection.isConnected()) {
                System.out.print("Tú: ");
                String input = scanner.nextLine();
                
                if ("salir".equalsIgnoreCase(input)) {
                    break;
                }
                
                connection.sendMessage(input);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("Desconectado");
    }
}
