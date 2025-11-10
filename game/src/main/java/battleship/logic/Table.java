package battleship.logic;

import java.util.List;

public class Table {
    private static final int SIZE = 10;
    private int[][] matrix;
    private int[][] myPlayerMatrix;

    public Table() {
        matrix = new int[SIZE][SIZE];
        myPlayerMatrix = new int[SIZE][SIZE];
    }

    public boolean checkRivalShot(Position position, List<Ship> myListOfShips) {
        if (position == null)
            throw new IllegalArgumentException("Postition cant't be null");
        if (myListOfShips == null)
            throw new IllegalArgumentException("List of Ships can't be null");
        if (myListOfShips.isEmpty())
            throw new IllegalStateException("List of ships can't be empty");
        for (Ship ship : myListOfShips) {
            if (ship.getPositions().contains(position)) {
                matrix[position.getX()][position.getY()] = 2;
                checkIfSunk(ship, matrix);
                return true;
            }
        }
        matrix[position.getX()][position.getY()] = 1;
        return false;
    }

    public boolean checkIfSunk(Ship ship, int[][] matrix) {
        boolean sunk = true;
        for (Position p : ship.getPositions()) {
            if (matrix[p.getX()][p.getY()] != 2) {
                sunk = false;
                break;
            }
        }
        if (sunk) {
            for (Position p : ship.getPositions()) {
                matrix[p.getX()][p.getY()] = 3; // marcar posiciones hundidas (amarillo)
            }
            System.out.println("Ship SUNK at positions: " + ship.getPositions());
        }
        return sunk;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getMyPlayerMatrix() {
        return myPlayerMatrix;
    }

    private boolean matrixACero(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void drowMyPlayerTable(List<Ship> listShips) {
        if (listShips == null)
            throw new IllegalArgumentException("List ships can't be null");
        if (listShips.isEmpty())
            throw new IllegalStateException("List of ships can't be empty");
        if (!matrixACero(myPlayerMatrix))
            throw new IllegalStateException("Matrix must be clean");
        for (Ship ship : listShips) {
            for (Position position : ship.getPositions()) {
                int x = position.getX();
                int y = position.getY();
                if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
                    throw new IllegalArgumentException("Position out of bounds: (" + x + ", " + y + ")");
                }
                myPlayerMatrix[x][y] = 1;// 1 barco, 0 agua
            }
        }
    }

}
