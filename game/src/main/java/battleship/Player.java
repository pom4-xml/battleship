package battleship;

import java.util.List;
import java.util.Scanner;

public class Player {
    private String name;
    private List<Ship> ships;

    public Player(String name, List<Ship> ships) {
        this.name = name;
        this.ships = ships;
    }

    public String getName() { 
        return name; 
    }

    public List<Ship> getShips() { 
        return ships; 
    }

    public boolean hasLost() {
        for (Ship s : ships) {
            if (!s.isSunk()) return false;
        }
        return true;
    }

    public String shootAt(Position p, Player enemy) {
        for (Ship s : enemy.getShips()) {
            if (s.occupies(p)) {
                s.registerHit(p);
                if (s.isSunk()) return "SUNK " + s.getClass().getSimpleName();
                return "HIT";
            }
        }
        return "MISS";
    }

    public void placeShipsManually(Scanner sc) {
        System.out.println("\n=== " + name + " placing ships ===");
        for (Ship s : ships) {
            boolean placed = false;
            while (!placed) {
                System.out.println("Place your " + s.getClass().getSimpleName() + " (size " + s.getSize() + ")");
                System.out.print("Starting row (0-9): ");
                int row = sc.nextInt();
                System.out.print("Starting column (0-9): ");
                int col = sc.nextInt();
                System.out.print("Horizontal? (true/false): ");
                boolean horizontal = sc.nextBoolean();

                List<Position> positions = Ship.generatePositions(row, col, s.getSize(), horizontal);

                boolean valid = true;
                for (Position p : positions) {
                    if (!Position.isValid(p.getX(), p.getY())) {
                        valid = false;
                        break;
                    }
                    for (Ship other : ships) {
                        if (other.getPositions().contains(p)) {
                            valid = false;
                            break;
                        }
                    }
                    if (!valid) break;
                }

                if (valid) {
                    s.setPositions(positions);
                    placed = true;
                } else {
                    System.out.println("Invalid position or overlapping ships. Try again.");
                }
            }
        }
    }
}
