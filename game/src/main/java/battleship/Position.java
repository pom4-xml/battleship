package battleship;

public class Position {
    private int x; // fila
    private int y; // columna
    public static final int MAX = 9; 
    public static final int MIN = 0; 

    public Position(int x, int y) {
        if (!esValida(x, y)) {
            throw new IllegalArgumentException(
                "Coordenada inválida: (" + x + ", " + y + ")"
            );
        }
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) {
        if (!esValida(x, this.y)) 
            throw new IllegalArgumentException("X fuera de rango: " + x);
        this.x = x;
    }

    public void setY(int y) {
        if (!esValida(this.x, y)) 
            throw new IllegalArgumentException("Y fuera de rango: " + y);
        this.y = y;
    }

    public static boolean esValida(int x, int y) {
        return x >= MIN && x <= MAX && y >= MIN && y <= MAX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return x * 31 + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
