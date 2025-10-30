package battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import battleship.ship.*;

class BattleshipManagerTest {

    @Test
    void testCreateStandardShips() {
        var ships = BattleshipManager.createStandardShips();
        assertEquals(5, ships.size());
        boolean hasDestroyer = false, hasSubmarine = false, hasCruiser = false, hasBattleship = false, hasCarrier = false;
        for (Ship s : ships) {
            if (s instanceof Destroyer) hasDestroyer = true;
            else if (s instanceof Submarine) hasSubmarine = true;
            else if (s instanceof Cruiser) hasCruiser = true;
            else if (s instanceof Battleship) hasBattleship = true;
            else if (s instanceof Carrier) hasCarrier = true;
        }
        assertTrue(hasDestroyer);
        assertTrue(hasSubmarine);
        assertTrue(hasCruiser);
        assertTrue(hasBattleship);
        assertTrue(hasCarrier);
    }
}
