package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    private Destroyer destroyer;
    private Position p1;
    private Position p2;
    private Position p3;

    @BeforeEach
    void setUp() {
        destroyer = new Destroyer();
        p1 = new Position(0, 0);
        p2 = new Position(0, 1);
        p3 = new Position(1, 1);
        destroyer.setPositions(Arrays.asList(p1, p2));
    }

    @Test
    void testOccupiesPosition() {
        assertTrue(destroyer.occupies(p1));
        assertFalse(destroyer.occupies(p3));
    }

    @Test
    void testRegisterHit() {
        assertTrue(destroyer.registerHit(p1));
        assertFalse(destroyer.registerHit(p3));
        assertEquals(1, destroyer.getHits().size());
    }

    @Test
    void testIsSunk() {
        destroyer.registerHit(p1);
        destroyer.registerHit(p2);
        assertTrue(destroyer.isSunk());
    }

    @Test
    void testNotSunkInitially() {
        assertFalse(destroyer.isSunk());
    }

    @Test
    void testGettersAndSetters() {
        destroyer.setSize(3);
        assertEquals(3, destroyer.getSize());
    }
}
