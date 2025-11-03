package battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import battleship.ship.Battleship;

class TableTest {

    private Table table;
    private List<Ship> ships;
    private Ship ship;

    @BeforeEach
    void setup() {
        table = new Table();
        ships = new ArrayList<>();

        ship = new Battleship();
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(1, 0));
        positions.add(new Position(2, 0));
        positions.add(new Position(3, 0));
        ship.setPositions(positions);

        ships.add(ship);
    }

    @Test
    void testShotHit() {
        boolean result = table.checkRivalShot(new Position(0, 0), ships);
        assertTrue(result);
        assertEquals(2, table.getMatrix()[0][0]);
    }

    @Test
    void testShotMiss() {
        boolean result = table.checkRivalShot(new Position(0, 1), ships);
        assertFalse(result);
        assertEquals(1, table.getMatrix()[0][1]);
    }

    @Test
    void testShotPositionNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> table.checkRivalShot(null, ships));
        assertEquals("Postition cant't be null", ex.getMessage());
    }

    @Test
    void testShotShipsListNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> table.checkRivalShot(new Position(0, 0), null));
        assertEquals("List of Ships can't be null", ex.getMessage());
    }

    @Test
    void testShotShipsListEmpty() {
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> table.checkRivalShot(new Position(0, 0), new ArrayList<>()));
        assertEquals("List of ships can't be empty", ex.getMessage());
    }

    @Test
    void testGetMatrixInitial() {
        int[][] matrix = table.getMatrix();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(0, matrix[i][j]);
            }
        }
    }
}
