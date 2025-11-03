package battleship;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player {
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
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
        LOGGER.log(Level.INFO, "\n=== {0} placing ships ===", name);
        for (Ship ship : ships) {
            placeSingleShip(sc, ship);
        }
    }

    private void placeSingleShip(Scanner sc, Ship ship) {
        boolean placed = false;
        while (!placed) {
            int row = getInput(sc, "Starting row (0-9): ");
            int col = getInput(sc, "Starting column (0-9): ");
            boolean horizontal = getBooleanInput(sc, "Horizontal? (true/false): ");

            List<Position> positions = Ship.generatePositions(row, col, ship.getSize(), horizontal);

            if (isValidPlacement(positions)) {
                ship.setPositions(positions);
                placed = true;
            } else {
                LOGGER.log(Level.WARNING, "Invalid position or overlapping ships. Try again.");
            }
        }
    }

    private boolean isValidPlacement(List<Position> positions) {
        for (Position p : positions) {
            if (!Position.isValid(p.getX(), p.getY())) return false;
            for (Ship other : ships) {
                if (other.getPositions().contains(p)) return false;
            }
        }
        return true;
    }

    private int getInput(Scanner sc, String message) {
        LOGGER.log(Level.INFO, message);
        return sc.nextInt();
    }

    private boolean getBooleanInput(Scanner sc, String message) {
        LOGGER.log(Level.INFO, message);
        return sc.nextBoolean();
    }
}
