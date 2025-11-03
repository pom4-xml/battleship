package battleship;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimulationGameController {
    private static final Logger LOGGER = Logger.getLogger(SimulationGameController.class.getName());
    private Player player1;
    private Player player2;
    private boolean player1Turn;
    private final Scanner scanner;

    public SimulationGameController() {
        scanner = new Scanner(System.in);
        player1Turn = true;
    }

    public void startGame() {
        LOGGER.info("=== Welcome to Battleship ===");
        player1 = new Player("Player 1", BattleshipManager.createStandardShips());
        player2 = new Player("Player 2", BattleshipManager.createStandardShips());
        placeShips(player1);
        placeShips(player2);
        gameLoop();
        scanner.close();
    }

    private void placeShips(Player player) {
        LOGGER.log(Level.INFO, "\n--- {0} placing ships ---", player.getName());
        for (Ship ship : player.getShips()) {
            while (!tryPlaceShip(player, ship)) {
                LOGGER.info("Invalid placement. Try again.");
            }
            printBoard(player, true);
        }
    }

    private boolean tryPlaceShip(Player player, Ship ship) {
        LOGGER.log(Level.INFO, "Place your {0} (size {1})",
                new Object[]{ship.getClass().getSimpleName(), ship.getSize()});
        LOGGER.info("Starting row (0-9): ");
        int row = scanner.nextInt();
        LOGGER.info("Starting column (0-9): ");
        int col = scanner.nextInt();
        LOGGER.info("Horizontal? (true/false): ");
        boolean horizontal = scanner.nextBoolean();

        if (!isPlacementValid(row, col, ship.getSize(), horizontal)) {
            LOGGER.info("Ship goes out of bounds. Try again.");
            return false;
        }

        List<Position> positions = Ship.generatePositions(row, col, ship.getSize(), horizontal);
        if (isOverlapping(player, positions)) {
            LOGGER.info("Ship overlaps with another ship. Try again.");
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

    private boolean isOverlapping(Player player, List<Position> positions) {
        for (Ship other : player.getShips()) {
            for (Position p : positions) {
                if (other.getPositions().contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameLoop() {
        while (true) {
            Player current = player1Turn ? player1 : player2;
            Player enemy = player1Turn ? player2 : player1;

            LOGGER.log(Level.INFO, "\n=== {0}''s turn ===", current.getName());
            printBoard(enemy, false);

            Position shot = askForShot();
            String result = current.shootAt(shot, enemy);
            LOGGER.log(Level.INFO, "Result: {0}", result);

            if (enemy.hasLost()) {
                LOGGER.log(Level.INFO, "\n🏆 {0} wins the game!", current.getName());
                break;
            }
            player1Turn = !player1Turn;
        }
    }

    private Position askForShot() {
        while (true) {
            LOGGER.info("Enter coordinates (row column): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if (!Position.isValid(x, y)) {
                LOGGER.info("Coordinates out of bounds. Try again.");
                continue;
            }
            return new Position(x, y);
        }
    }

    private void printBoard(Player player, boolean showShips) {
        char[][] grid = new char[10][10];
        for (int i = 0; i < 10; i++) Arrays.fill(grid[i], '.');
        fillGridWithShips(player, grid, showShips);

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(buildBoardString(player, showShips, grid));
        }
    }

    private void fillGridWithShips(Player player, char[][] grid, boolean showShips) {
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
    }

    private String buildBoardString(Player player, boolean showShips, char[][] grid) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%n%s %s%n", player.getName(), showShips ? "(your ships)" : "board"));
        sb.append("   ");
        for (int j = 0; j < 10; j++) sb.append(String.format("%d ", j));
        sb.append("\n");
        for (int i = 0; i < 10; i++) {
            sb.append(String.format("%d ", i));
            for (int j = 0; j < 10; j++) sb.append(String.format("%c ", grid[i][j]));
            sb.append("\n");
        }
        return sb.toString();
    }
}