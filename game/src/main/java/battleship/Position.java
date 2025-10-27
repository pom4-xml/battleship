package battleship;

public class Position {
    private int x; // fila
    private int y; // columna
    public static final int MAX = 9; // índice máximo (0-9 para un tablero 10x10)
    public static final int MIN = 0; // índice mínimo

    public Position(int x, int y) {
        if (!esValida(x, y)) {
            throw new IllegalArgumentException(
                "Coordenada inválida. Debe estar entre 0 y 9. Recibido: (" + x + ", " + y + ")"
            );
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        if (!esValida(x, this.y)) {
            throw new IllegalArgumentException("Coordenada X fuera de rango: " + x);
        }
        this.x = x;
    }

    public void setY(int y) {
        if (!esValida(this.x, y)) {
            throw new IllegalArgumentException("Coordenada Y fuera de rango: " + y);
        }
        this.y = y;
    }

    public static boolean esValida(int x, int y) {
        return x >= MIN && x <= MAX && y >= MIN && y <= MAX;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}

