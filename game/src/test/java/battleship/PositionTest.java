package battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    void testValidPositionCreation() {
        Position pos = new Position(3, 5);
        assertEquals(3, pos.getX());
        assertEquals(5, pos.getY());
    }

    @Test
    void testInvalidPositionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Position(3, 10));
    }

    @Test
    void testIsValidMethod() {
        assertTrue(Position.isValid(0, 0));
        assertTrue(Position.isValid(9, 9));
        assertFalse(Position.isValid(-1, 5));
        assertFalse(Position.isValid(10, 0));
    }

    @Test
    void testEqualsAndHashCode() {
        Position p1 = new Position(4, 7);
        Position p2 = new Position(4, 7);
        Position p3 = new Position(5, 7);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testNotEqualDifferentObject() {
        Position p = new Position(2, 2);
        assertNotEquals(p, "string");
        assertNotEquals(p, null);
    }
}
