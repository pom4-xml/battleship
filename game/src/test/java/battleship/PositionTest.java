package battleship;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

 class PositionTest {   


    @Test
    void testEsValida() {

        assertTrue(Position.isValid(0, 0));
        assertTrue(Position.isValid(5, 5));
        assertTrue(Position.isValid(9, 9));

        assertFalse(Position.isValid(-1, 0));
        assertFalse(Position.isValid(0, -1));
        assertFalse(Position.isValid(10, 5));
        assertFalse(Position.isValid(5, 10));
    }
}
