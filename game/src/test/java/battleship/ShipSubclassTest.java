package battleship;

import org.junit.jupiter.api.Test;

import battleship.ship.Battleship;
import battleship.ship.Carrier;
import battleship.ship.Cruiser;
import battleship.ship.Destroyer;
import battleship.ship.Submarine;

import static org.junit.jupiter.api.Assertions.*;

class ShipTypesTest {

    @Test
    void testShipSizes() {
        assertEquals(2, new Destroyer().getSize());
        assertEquals(3, new Submarine().getSize());
        assertEquals(3, new Cruiser().getSize());
        assertEquals(4, new Battleship().getSize());
        assertEquals(5, new Carrier().getSize());
    }
}
