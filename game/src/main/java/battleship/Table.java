package battleship;

import java.util.List;

public class Table {
    private static final int SIZE = 10;
    private int [][] matrix;
    

    public Table(){
        matrix = new int[SIZE][SIZE];
    }

    public boolean checkRivalShot(Position position,List<Ship> myListOfShips){
        if(position == null) return false;
        if(myListOfShips == null || myListOfShips.isEmpty()) return false;
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
