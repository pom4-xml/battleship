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

    public String getName() { return name; }
    public List<Ship> getShips() { return ships; }

    public boolean hasLost() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    public String shootAt(Position p, Player enemy) {
        for (Ship s : enemy.getShips()) {
            if (s.occupies(p)) {
                s.registerHit(p);
                return s.isSunk() ? "SUNK" : "HIT";
            }
        }
        return "MISS";
    }

    public void placeShipsManually(Scanner sc) {
        System.out.println("=== " + name + " coloca sus barcos ===");
        for (Ship s : ships) {
            System.out.println("Coloca tu " + s.getClass().getSimpleName() + " (tamaño " + s.getSize() + ")");
            System.out.print("Fila inicial (0-9): ");
            int fila = sc.nextInt();
            System.out.print("Columna inicial (0-9): ");
            int col = sc.nextInt();
            System.out.print("Horizontal? (true/false): ");
            boolean horizontal = sc.nextBoolean();

            s.setPositions(Ship.generarPosiciones(fila, col, s.getSize(), horizontal));
        }
    }
}
