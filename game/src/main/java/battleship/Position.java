package battleship;

public class Position {
    private int x;
    private int y;
    public static final int MIN = 0;
    public static final int MAX = 9; // 10x10 board

    public Position(int x, int y) {
        if (!isValid(x, y)) {
            throw new IllegalArgumentException("Coordinates out of range: (" + x + ", " + y + ")");
        }
        this.x = x;
        this.y = y;
    }

    public static boolean isValid(int x, int y) {
        return x >= 0 && x <= 9 && y >= 0 && y <= 9;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Position))
            return false;
        Position p = (Position) obj;
        return x == p.x && y == p.y;
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        return x * 31 + y;
    }
}