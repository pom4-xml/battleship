package battleship;

import java.util.*;

public class SimulationGameController {
    private Player player1;
    private Player player2;
    private boolean player1Turn;
    private Scanner scanner;

    public SimulationGameController() {
        scanner = new Scanner(System.in);
        player1Turn = true;
    }

    public void startGame() {
        System.out.println("=== Welcome to Battleship ===");

        player1 = new Player("Player 1", BattleshipManager.createStandardShips());
        player2 = new Player("Player 2", BattleshipManager.createStandardShips());

        placeShips(player1);
        placeShips(player2);

        gameLoop();

        scanner.close();
    }

    private void placeShips(Player player) {
        System.out.println("\n--- " + player.getName() + " placing ships ---");
        for (Ship ship : player.getShips()) {
            boolean placed = false;
            while (!placed) {
                System.out.println("Place your " + ship.getClass().getSimpleName() + " (size " + ship.getSize() + ")");
                System.out.print("Starting row (0-9): ");
                int row = scanner.nextInt();
                System.out.print("Starting column (0-9): ");
                int col = scanner.nextInt();
                System.out.print("Horizontal? (true/false): ");
                boolean horizontal = scanner.nextBoolean();

                int maxRow = horizontal ? row : row + ship.getSize() - 1;
                int maxCol = horizontal ? col + ship.getSize() - 1 : col;
                if (maxRow > Position.MAX || maxCol > Position.MAX) {
                    System.out.println("Ship goes out of bounds. Try again.");
                    continue;
                }

                List<Position> positions = Ship.generatePositions(row, col, ship.getSize(), horizontal);

                boolean overlaps = false;
                for (Position p : positions) {
                    for (Ship other : player.getShips()) {
                        if (other.getPositions().contains(p)) {
                            overlaps = true;
                            break;
                        }
                    }
                    if (overlaps) break;
                }

                if (!overlaps) {
                    ship.setPositions(positions);
                    placed = true;
                } else {
                    System.out.println("Ship overlaps with another ship. Try again.");
                }
            }
            printBoard(player, true);
        }
    }

    private void gameLoop() {
        while (true) {
            Player current = player1Turn ? player1 : player2;
            Player enemy = player1Turn ? player2 : player1;

            System.out.println("\n=== " + current.getName() + "'s turn ===");
            printBoard(enemy, false);

            Position shot = askForShot(current);
            String result = current.shootAt(shot, enemy);
            System.out.println("Result: " + result);

            if (enemy.hasLost()) {
                System.out.println("\n🏆 " + current.getName() + " wins the game!");
                break;
            }

            player1Turn = !player1Turn;
        }
    }

    private Position askForShot(Player player) {
        while (true) {
            System.out.print("Enter coordinates (row column): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
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
                if (s.getHits().contains(p)) grid[p.getX()][p.getY()] = 'X';
                else if (showShips) grid[p.getX()][p.getY()] = 'O';
            }
        }

        System.out.println("\n" + player.getName() + (showShips ? " (your ships)" : " board"));
        System.out.print("  ");
        for (int j = 0; j < 10; j++) System.out.print(j + " ");
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 10; j++) System.out.print(grid[i][j] + " ");
            System.out.println();
        }
    }
}
