package battleship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Ship {
    protected int size;
    protected List<Position> positions;
    protected Set<Position> hits;

    protected Ship(int size) {
        this.size = size;
        this.positions = new ArrayList<>();
        this.hits = new HashSet<>();
    }

    public boolean occupies(Position p) {
        return positions.contains(p);
    }

    public boolean registerHit(Position p) {
        if (occupies(p)) {
            hits.add(p);
            return true;
        }
        return false;
    }

    public boolean isSunk() {
        return hits.size() == size;
    }

    public static List<Position> generarPosiciones(int fila, int col, int tamaño, boolean horizontal) {
        List<Position> pos = new ArrayList<>();
        for (int i = 0; i < tamaño; i++) {
            int x = horizontal ? fila : fila + i;
            int y = horizontal ? col + i : col;
            if (!Position.esValida(x, y)) {
                throw new IllegalArgumentException("Posición fuera del tablero: (" + x + "," + y + ")");
            }
            pos.add(new Position(x, y));
        }
        return pos;
    }

    // Getters y Setters
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public List<Position> getPositions() { return positions; }
    public void setPositions(List<Position> positions) { this.positions = positions; }
    public Set<Position> getHits() { return hits; }
    public void setHits(Set<Position> hits) { this.hits = hits; }
}
