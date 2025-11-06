package battleship;

import battleship.communication.Communication;
import battleship.communication.Message;
import battleship.communication.Message.MessageType;
import battleship.communication.socket.Connection;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador multiplayer que extiende la lógica de SimulationGameController
 * añadiendo comunicación por red entre dos ordenadores.
 */
public class MultiplayerGameController {
    private static final Logger LOGGER = Logger.getLogger(MultiplayerGameController.class.getName());
    
    private Player localPlayer;
    private Player remotePlayer;
    private Connection connection;
    private boolean isLocalTurn;
    private final Scanner scanner;
    private MessageReceiver messageReceiver;

    public MultiplayerGameController() {
        scanner = new Scanner(System.in);
        isLocalTurn = true;
    }

    /**
     * Inicia el juego multiplayer:
     * 1. Establece conexión (cliente o servidor automáticamente)
     * 2. Ambos colocan sus barcos
     * 3. Intercambian confirmación de listos
     * 4. Comienzan a jugar
     */
    public void startGame() {
        LOGGER.info("=== Battleship Multiplayer ===");
        
        // Paso 1: Establecer conexión
        if (!establishConnection()) {
            LOGGER.severe("No se pudo establecer conexión");
            return;
        }
        
        // Paso 2: Crear jugadores
        localPlayer = new Player("You", BattleshipManager.createStandardShips());
        remotePlayer = new Player("Opponent", BattleshipManager.createStandardShips());
        
        // Paso 3: Empezar receptor de mensajes en background
        messageReceiver = new MessageReceiver(connection, this);
        new Thread(messageReceiver, "message-receiver").start();
        
        // Paso 4: Colocar barcos localmente
        placeShipsLocally();
        
        // Paso 5: Notificar al oponente que estamos listos
        sendMessage(new Message(MessageType.JOIN, "READY"));
        LOGGER.info("Esperando a que el oponente coloque sus barcos...");
        
        // Paso 6: Esperar a que el oponente esté listo
        waitForOpponentReady();
        
        // Paso 7: Determinar quién empieza (servidor siempre comienza)
        if (connection instanceof battleship.communication.socket.Server) {
            isLocalTurn = true;
            sendMessage(new Message(MessageType.SERVER_TURN, ""));
            LOGGER.info("Tú comienzas");
        } else {
            isLocalTurn = false;
            LOGGER.info("El oponente comienza");
        }
        
        // Paso 8: Loop de juego
        gameLoop();
        
        scanner.close();
    }

    private boolean establishConnection() {
        Communication comm = new Communication();
        connection = comm.establishConnection();
        return connection != null && connection.isConnected();
    }

    private void placeShipsLocally() {
        LOGGER.info("\n--- Coloca tus barcos ---");
        for (Ship ship : localPlayer.getShips()) {
            while (!tryPlaceShip(ship)) {
                LOGGER.info("Posición inválida. Intenta de nuevo.");
            }
            printBoard(localPlayer, true);
        }
    }

    private boolean tryPlaceShip(Ship ship) {
        LOGGER.log(Level.INFO, "Coloca tu {0} (tamaño {1})",
                new Object[]{ship.getClass().getSimpleName(), ship.getSize()});
        LOGGER.info("Fila inicial (0-9): ");
        int row = scanner.nextInt();
        LOGGER.info("Columna inicial (0-9): ");
        int col = scanner.nextInt();
        LOGGER.info("¿Horizontal? (true/false): ");
        boolean horizontal = scanner.nextBoolean();

        if (!isPlacementValid(row, col, ship.getSize(), horizontal)) {
            LOGGER.info("El barco se sale del tablero.");
            return false;
        }

        List<Position> positions = Ship.generatePositions(row, col, ship.getSize(), horizontal);
        if (isOverlapping(positions)) {
            LOGGER.info("El barco se superpone con otro.");
            return false;
        }

        ship.setPositions(positions);
        return true;
    }

    private boolean isPlacementValid(int row, int col, int size, boolean horizontal) {
        int maxRow = horizontal ? row : row + size - 1;
        int maxCol = horizontal ? col + size - 1 : col;
        return maxRow <= Position.MAX && maxCol <= Position.MAX;
    }

    private boolean isOverlapping(List<Position> positions) {
        for (Ship other : localPlayer.getShips()) {
            for (Position p : positions) {
                if (other.getPositions().contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void waitForOpponentReady() {
        while (!remotePlayer.getShips().isEmpty() && remotePlayer.getShips().get(0).getPositions().isEmpty()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        LOGGER.info("¡El oponente está listo!");
    }

    private void gameLoop() {
        while (!localPlayer.hasLost() && !remotePlayer.hasLost()) {
            if (isLocalTurn) {
                LOGGER.info("\n=== Tu turno ===");
                printBoard(remotePlayer, false);
                
                Position shot = askForShot();
                String result = localPlayer.shootAt(shot, remotePlayer);
                LOGGER.log(Level.INFO, "Resultado: {0}", result);
                
                // Enviar ataque y resultado al oponente
                sendMessage(new Message(MessageType.ATTACK, shot.getX() + "," + shot.getY() + "," + result));
                
                // Cambiar turno
                isLocalTurn = false;
                sendMessage(new Message(MessageType.CLIENT_TURN, ""));
            } else {
                LOGGER.info("\n=== Turno del oponente ===");
                try {
                    Thread.sleep(1000); // Esperar un poco antes de revisar
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        // Determinar ganador
        if (remotePlayer.hasLost()) {
            LOGGER.info("\n🏆 ¡GANASTE!");
        } else {
            LOGGER.info("\n💀 PERDISTE");
        }
    }

    private Position askForShot() {
        while (true) {
            LOGGER.info("Ingresa coordenadas (fila columna): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if (!Position.isValid(x, y)) {
                LOGGER.info("Coordenadas fuera del tablero.");
                continue;
            }
            return new Position(x, y);
        }
    }

    private void printBoard(Player player, boolean showShips) {
        char[][] grid = new char[10][10];
        for (int i = 0; i < 10; i++) Arrays.fill(grid[i], '.');
        
        for (Ship s : player.getShips()) {
            for (Position p : s.getPositions()) {
                char symbol;
                if (s.getHits().contains(p)) {
                    symbol = 'X';
                } else if (showShips) {
                    symbol = 'O';
                } else {
                    symbol = grid[p.getX()][p.getY()];
                }
                grid[p.getX()][p.getY()] = symbol;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%n%s %s%n", player.getName(), showShips ? "(tus barcos)" : "tablero"));
        sb.append("   ");
        for (int j = 0; j < 10; j++) sb.append(String.format("%d ", j));
        sb.append("\n");
        for (int i = 0; i < 10; i++) {
            sb.append(String.format("%d ", i));
            for (int j = 0; j < 10; j++) sb.append(String.format("%c ", grid[i][j]));
            sb.append("\n");
        }
        LOGGER.info(sb.toString());
    }

    public void sendMessage(Message msg) {
        try {
            connection.sendMessage(msg.serialize());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error enviando mensaje: " + e.getMessage(), e);
        }
    }

    public void handleIncomingMessage(Message msg) {
        switch (msg.getType()) {
            case JOIN:
                LOGGER.info("Oponente listo");
                break;
            case ATTACK:
                handleRemoteAttack(msg.getPayload());
                break;
            case SERVER_TURN:
                isLocalTurn = false;
                break;
            case CLIENT_TURN:
                isLocalTurn = true;
                break;
            default:
                break;
        }
    }

    private void handleRemoteAttack(String payload) {
        String[] parts = payload.split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        String result = parts[2];

        LOGGER.log(Level.INFO, "Oponente atacó ({0},{1}): {2}", new Object[]{row, col, result});
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public Player getRemotePlayer() {
        return remotePlayer;
    }

    public Connection getConnection() {
        return connection;
    }
}