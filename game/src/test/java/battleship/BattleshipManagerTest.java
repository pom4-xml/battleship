package battleship;

import org.junit.jupiter.api.Test;

import battleship.ship.Battleship;
import battleship.ship.Carrier;
import battleship.ship.Cruiser;
import battleship.ship.Destroyer;
import battleship.ship.Submarine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattleshipManagerTest {

    @Test
    void testCreateStandardShips() {
        List<Ship> ships = BattleshipManager.createStandardShips();

        assertEquals(5, ships.size());

        assertTrue(containsType(ships, Destroyer.class), "Falta Destroyer");
        assertTrue(containsType(ships, Submarine.class), "Falta Submarine");
        assertTrue(containsType(ships, Cruiser.class), "Falta Cruiser");
        assertTrue(containsType(ships, Battleship.class), "Falta Battleship");
        assertTrue(containsType(ships, Carrier.class), "Falta Carrier");
    }
    
    private boolean containsType(List<Ship> ships, Class<? extends Ship> type) {
        return ships.stream().anyMatch(type::isInstance);
    }
}
