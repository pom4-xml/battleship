package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import battleship.logic.Position;
import battleship.logic.Ship;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    private Ship testShip;
    private Position p1, p2, p3;

    static class TestShip extends Ship {
        public TestShip(int size) {
            super(size);
        }
    }

    @BeforeEach
    void setUp() {
        testShip = new TestShip(2);
        p1 = new Position(0, 0);
        p2 = new Position(0, 1);
        p3 = new Position(1, 1);
        testShip.setPositions(Arrays.asList(p1, p2));
    }

    @Test
    void testOccupies() {
        assertTrue(testShip.occupies(p1));
        assertFalse(testShip.occupies(p3));
    }

    @Test
    void testRegisterHit() {
        assertTrue(testShip.registerHit(p1));
        assertFalse(testShip.registerHit(p3));
        assertEquals(1, testShip.getHits().size());
    }

    @Test
    void testIsSunk() {
        testShip.registerHit(p1);
        assertFalse(testShip.isSunk());
        testShip.registerHit(p2);
        assertTrue(testShip.isSunk());
    }

    @Test
    void testGettersAndSetters() {
        testShip.setSize(3);
        assertEquals(3, testShip.getSize());

        List<Position> newPositions = Arrays.asList(p1, p2, p3);
        testShip.setPositions(newPositions);
        assertEquals(newPositions, testShip.getPositions());

        Set<Position> newHits = new HashSet<>(Collections.singletonList(p1));
        testShip.setHits(newHits);
        assertEquals(newHits, testShip.getHits());
    }
}
