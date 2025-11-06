package battleship;

import java.io.PrintStream;
import java.util.*;

public class SimulationGameController {
    private Player player1;
    private Player player2;
    private boolean player1Turn;

    public SimulationGameController() {
        this.player1Turn = true;
    }

    public void startGame() {
        System.out.println("=== Welcome to Battleship ===");
        player1 = new Player("Player 1", BattleshipManager.createStandardShips());
        player2 = new Player("Player 2", BattleshipManager.createStandardShips());

        try (Scanner scanner = new Scanner(System.in)) {
            placeShips(player1, scanner);
            placeShips(player2, scanner);
            gameLoop(scanner);
        } catch (Exception e) {
            System.err.println("⚠ Error during game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void placeShips(Player player, Scanner scanner) {
        System.out.println("\n--- " + player.getName() + " placing ships ---");
        for (Ship ship : player.getShips()) {
            boolean placed = false;
            while (!placed) {
                placed = tryPlaceShip(player, ship, scanner);
                if (!placed) System.out.println("Invalid placement. Try again.");
            }
            printBoard(player, true);
        }
    }

    private boolean tryPlaceShip(Player player, Ship ship, Scanner scanner) {
        System.out.println("Place your " + ship.getClass().getSimpleName() + " (size " + ship.getSize() + ")");
        int row = safeNextInt(scanner, "Starting row (0-9): ");
        int col = safeNextInt(scanner, "Starting column (0-9): ");
        boolean horizontal = safeNextBoolean(scanner, "Horizontal? (true/false): ");

        if (!isPlacementValid(row, col, ship.getSize(), horizontal)) {
            System.out.println("Ship goes out of bounds. Try again.");
            return false;
        }

        List<Position> positions = Ship.generatePositions(row, col, ship.getSize(), horizontal);
        if (isOverlapping(player, positions)) {
            System.out.println("Ship overlaps with another ship. Try again.");
            return false;
        }

        ship.setPositions(positions);
        return true;
    }

    private int safeNextInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }

    private boolean safeNextBoolean(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextBoolean()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        return scanner.nextBoolean();
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

    private void gameLoop(Scanner scanner) {
        while (true) {
            Player current = player1Turn ? player1 : player2;
            Player enemy = player1Turn ? player2 : player1;

            System.out.println("\n=== " + current.getName() + "'s turn ===");
            printBoard(enemy, false);

            Position shot = askForShot(scanner);
            String result = current.shootAt(shot, enemy);
            System.out.println("Result: " + result);

            if (enemy.hasLost()) {
                System.out.println("\n🏆 " + current.getName() + " wins the game!");
                break;
            }
            player1Turn = !player1Turn;
        }
    }

    private Position askForShot(Scanner scanner) {
        while (true) {
            int x = safeNextInt(scanner, "Enter row (0-9): ");
            int y = safeNextInt(scanner, "Enter column (0-9): ");
            if (!Position.isValid(x, y)) {
                System.out.println("Coordinates out of bounds. Try again.");
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
                if (s.getHits().contains(p)) {
                    grid[p.getX()][p.getY()] = 'X';
                } else if (showShips) {
                    grid[p.getX()][p.getY()] = 'O';
                }
            }
        }

        System.out.println("\n" + player.getName() + (showShips ? " (your ships)" : " (enemy board)"));
        System.out.print("   ");
        for (int j = 0; j < 10; j++) System.out.print(j + " ");
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 10; j++) System.out.print(grid[i][j] + " ");
            System.out.println();
        }
    }
}