package battleship;

import java.util.List;

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

    // Nuevo método para dibujar tablero
    public void printBoard(boolean showShips) {
        char[][] board = new char[10][10];

        // Inicializar tablero con agua
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                board[i][j] = '~';

        // Colocar barcos
        if (showShips) {
            for (Ship s : ships) {
                for (Position p : s.getPositions()) {
                    board[p.getX()][p.getY()] = 'B';
                }
            }
        }

        // Marcar hits y misses
        for (Ship s : ships) {
            for (Position p : s.getHits()) {
                board[p.getX()][p.getY()] = 'X';
            }
        }

        // Imprimir tablero
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void placeShipsManually(java.util.Scanner sc) {
        System.out.println("=== " + name + " coloca sus barcos ===");
        for (Ship s : ships) {
            boolean colocado = false;
            while (!colocado) {
                System.out.println("Coloca tu " + s.getClass().getSimpleName() + " (tamaño " + s.getSize() + ")");
                System.out.print("Fila inicial (0-9): ");
                int fila = sc.nextInt();
                System.out.print("Columna inicial (0-9): ");
                int col = sc.nextInt();
                System.out.print("Horizontal? (true/false): ");
                boolean horizontal = sc.nextBoolean();

                List<Position> nuevasPos = Ship.generarPosiciones(fila, col, s.getSize(), horizontal);

                boolean overlap = false;
                for (Position p : nuevasPos) {
                    for (Ship otro : ships) {
                        if (otro.getPositions().contains(p)) {
                            overlap = true;
                            break;
                        }
                    }
                    if (overlap) break;
                }

                if (!overlap) {
                    s.setPositions(nuevasPos);
                    colocado = true;
                    printBoard(true); // Mostrar tablero después de colocar cada barco
                } else {
                    System.out.println("Error: el barco se superpone con otro. Intenta de nuevo.");
                }
            }
        }
    }
}
