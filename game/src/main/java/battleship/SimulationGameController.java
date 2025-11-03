package battleship;

import java.util.*;
import java.util.logging.Logger;

public class SimulationGameController {
    private static final Logger LOGGER = Logger.getLogger(SimulationGameController.class.getName());
    private Player player1;
    private Player player2;
    private boolean player1Turn;
    private Scanner scanner;

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
        LOGGER.info("\n--- " + player.getName() + " placing ships ---");
        for (Ship ship : player.getShips()) {
            placeSingleShip(player, ship);
            printBoard(player, true);
        }
    }

    private void placeSingleShip(Player player, Ship ship) {
        boolean placed = false;
        while (!placed) {
            ShipPlacement placement = askForShipPlacement(ship);
            if (!isPlacementValid(placement, ship, player)) {
                LOGGER.warning("Invalid position or overlapping ships. Try again.");
                continue;
            }
            ship.setPositions(Ship.generatePositions(placement.getRow(), placement.getCol(), ship.getSize(), placement.isHorizontal()));
            placed = true;
        }
    }

    private ShipPlacement askForShipPlacement(Ship ship) {
        LOGGER.info("Place your " + ship.getClass().getSimpleName() + " (size " + ship.getSize() + ")");
        LOGGER.info("Starting row (0-9): ");
        int row = scanner.nextInt();
        LOGGER.info("Starting column (0-9): ");
        int col = scanner.nextInt();
        LOGGER.info("Horizontal? (true/false): ");
        boolean horizontal = scanner.nextBoolean();
        return new ShipPlacement(row, col, horizontal);
    }

    private boolean isPlacementValid(ShipPlacement placement, Ship ship, Player player) {
        int maxRow = placement.isHorizontal() ? placement.getRow() : placement.getRow() + ship.getSize() - 1;
        int maxCol = placement.isHorizontal() ? placement.getCol() + ship.getSize() - 1 : placement.getCol();

        if (maxRow > Position.MAX || maxCol > Position.MAX) {
            LOGGER.warning("Ship goes out of bounds.");
            return false;
        }

        List<Position> positions = Ship.generatePositions(placement.getRow(), placement.getCol(), ship.getSize(), placement.isHorizontal());
        for (Position p : positions) {
            if (!Position.isValid(p.getX(), p.getY())) return false;
            for (Ship other : player.getShips()) {
                if (other.getPositions().contains(p)) return false;
            }
        }
        return true;
    }

    private static class ShipPlacement {
        private final int row;
        private final int col;
        private final boolean horizontal;

        public ShipPlacement(int row, int col, boolean horizontal) {
            this.row = row;
            this.col = col;
            this.horizontal = horizontal;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public boolean isHorizontal() {
            return horizontal;
        }
    }

    private void gameLoop() {
        while (true) {
            Player current = player1Turn ? player1 : player2;
            Player enemy = player1Turn ? player2 : player1;

            LOGGER.info("\n=== " + current.getName() + "'s turn ===");
            printBoard(enemy, false);

            Position shot = askForShot();
            String result = current.shootAt(shot, enemy);
            LOGGER.info("Result: " + result);

            if (enemy.hasLost()) {
                LOGGER.info("\n🏆 " + current.getName() + " wins the game!");
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
                LOGGER.warning("Coordinates out of bounds. Try again.");
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
                if (s.getHits().contains(p)) grid[p.getX()][p.getY()] = 'X';
                else if (showShips) grid[p.getX()][p.getY()] = 'O';
            }
        }

        LOGGER.info("\n" + player.getName() + (showShips ? " (your ships)" : " board"));
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int j = 0; j < 10; j++) sb.append(j).append(" ");
        sb.append("\n");
        for (int i = 0; i < 10; i++) {
            sb.append(i).append(" ");
            for (int j = 0; j < 10; j++) sb.append(grid[i][j]).append(" ");
            sb.append("\n");
        }
        LOGGER.info(sb.toString());
    }
}
