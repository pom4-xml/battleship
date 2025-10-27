package battleship;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Ship {
    int size;
    List<Position> positions;
    Set<Position> hits;

    protected Ship(int size) {
        this.size = size;
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

    // Getters and Setters

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Set<Position> getHits() {
        return hits;
    }

    public void setHits(Set<Position> hits) {
        this.hits = hits;
    }
}
