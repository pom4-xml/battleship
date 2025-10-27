package battleship;


public class Table {
    private static final int SIZE = 10;
    private int [][] matrix;

    public Table(){
        matrix = new int[SIZE][SIZE];
    }

    public int[][] getMatrix() {
        return matrix;
    }

}
