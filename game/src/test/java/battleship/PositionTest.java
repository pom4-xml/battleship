package battleship;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

 class PositionTest {
    @Test
    void testEsValida() {

        assertTrue(Position.esValida(0, 0));
        assertTrue(Position.esValida(5, 5));
        assertTrue(Position.esValida(9, 9));

        assertFalse(Position.esValida(-1, 0));
        assertFalse(Position.esValida(0, -1));
        assertFalse(Position.esValida(10, 5));
        assertFalse(Position.esValida(5, 10));
    }
}
