package battleship;

import java.util.List;

public class Table {
    private static final int SIZE = 10;
    private int [][] matrix;
    

    public Table(){
        matrix = new int[SIZE][SIZE];
    }

    public boolean checkRivalShot(Position position,List<Ship> myListOfShips){
        if(position == null) throw new IllegalArgumentException("Postition cant't be null");
        if(myListOfShips == null) throw new IllegalArgumentException("List of Ships can't be null");
        if(myListOfShips.isEmpty()) throw new IllegalStateException("List of ships can't be empty");
        for(Ship ship : myListOfShips){
            if(ship.getPositions().contains(position)){
                matrix [position.getX()][position.getY()] = 2;
                return true;
            }
        }
        matrix [position.getX()][position.getY()] = 1;
        return false;
    }

    public int[][] getMatrix() {
        return matrix;
    }

}
