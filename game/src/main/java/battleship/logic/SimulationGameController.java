package battleship.logic;

import java.util.*;
import battleship.swing.BattleshipFrame;

public class SimulationGameController {
    private Player player1;
    private Player player2;
    private Table table1;
    private Table table2;
    private boolean player1Turn;
    final Scanner scanner;

    public SimulationGameController() {
        scanner = new Scanner(System.in);
        player1Turn = true;
    }

    public void startGame() {
        System.out.println("=== Welcome to Battleship ===");
        player1 = new Player("Player 1", BattleshipManager.createStandardShips());
        player2 = new Player("Player 2", BattleshipManager.createStandardShips());
        table1 = new Table();
        table2 = new Table();

        placeShips(player1, table1);
        placeShips(player2, table2);

        // Crear un solo frame por jugador
        BattleshipFrame frame1 = new BattleshipFrame(table1);
        BattleshipFrame frame2 = new BattleshipFrame(table2);

        gameLoop(frame1, frame2);
        scanner.close();
    }

    public void placeShips(Player player, Table table) {
        System.out.println("\n--- " + player.getName() + " placing ships ---");
        for (Ship ship : player.getShips()) {
            while (!tryPlaceShip(player, ship)) {
                System.out.println("Invalid placement. Try again.");
            }
        }
        table.drowMyPlayerTable(player.getShips());
    }

    private boolean tryPlaceShip(Player player, Ship ship) {
        System.out.printf("Place your %s (size %d)%n",
                ship.getClass().getSimpleName(), ship.getSize());
        System.out.print("Starting row (0-9): ");
        int row = scanner.nextInt();
        System.out.print("Starting column (0-9): ");
        int col = scanner.nextInt();
        System.out.print("Horizontal? (true/false): ");
        boolean horizontal = scanner.nextBoolean();

        // ✅ Corrige la orientación: ahora horizontal = hacia la derecha, vertical = hacia abajo
        int maxRow = horizontal ? row : row + ship.getSize() - 1;
        int maxCol = horizontal ? col + ship.getSize() - 1 : col;
        if (maxRow > Position.MAX || maxCol > Position.MAX) {
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

    public boolean isOverlapping(Player player, List<Position> positions) {
        for (Ship other : player.getShips()) {
            for (Position p : positions) {
                if (other.getPositions().contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void gameLoop(BattleshipFrame frame1, BattleshipFrame frame2) {
        while (true) {
            Player current = player1Turn ? player1 : player2;
            Player enemy = player1Turn ? player2 : player1;
            Table enemyTable = player1Turn ? table2 : table1;

            System.out.println("\n=== " + current.getName() + "'s turn ===");

            Position shot = askForShot();

            // ✅ Usa tu método checkRivalShot
            boolean hit = enemyTable.checkRivalShot(shot, enemy.getShips());
            System.out.println("Result: " + (hit ? "HIT" : "MISS"));

            // 🔄 refresca los tableros ya existentes
            javax.swing.SwingUtilities.invokeLater(() -> {
                frame1.refreshBoard();
                frame2.refreshBoard();
            });

            // ✅ comprueba si todos los barcos del enemigo han sido destruidos
            if (allShipsDestroyed(enemy, enemyTable)) {
                System.out.println("\n🏆 " + current.getName() + " wins the game!");
                break;
            }

            player1Turn = !player1Turn;
        }
    }

    public boolean allShipsDestroyed(Player enemy, Table enemyTable) {
        for (Ship ship : enemy.getShips()) {
            for (Position p : ship.getPositions()) {
                if (enemyTable.getMatrix()[p.getX()][p.getY()] != 2) {
                    return false;
                }
            }
        }
        return true;
    }

    public Position askForShot() {
        while (true) {
            System.out.print("Enter shot coordinates (row column): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if (!Position.isValid(x, y)) {
                System.out.println("Coordinates out of bounds. Try again.");
                continue;
            }
            return new Position(x, y);
        }
    }
}
