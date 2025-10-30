package battleship;

import java.util.List;

public class SimulationBoard {
    private List<Ship> ships;

    public SimulationBoard(List<Ship> ships) {
        this.ships = ships;
    }

    // Devuelve "HIT", "MISS" o "SUNK"
    public String processShot(Position p) {
        for (Ship ship : ships) {
            if (ship.occupies(p)) {
                ship.registerHit(p);
                if (ship.isSunk()) return "SUNK";
                return "HIT";
            }
        }
        return "MISS";
    }

    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }
}
