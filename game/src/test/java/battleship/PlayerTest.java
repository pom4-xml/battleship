package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import battleship.ship.Battleship;
import battleship.ship.Cruiser;
import battleship.ship.Destroyer;
import battleship.ship.Submarine;
import battleship.ship.Carrier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;
    Player enemy;
    Ship destroyer;
    Position p1, p2;

    @BeforeEach
    void setup() {
        destroyer = new Destroyer();
        p1 = new Position(0, 0);
        p2 = new Position(0, 1);
        destroyer.setPositions(List.of(p1, p2));

        player = new Player("Player 1", List.of(destroyer));
        enemy = new Player("Player 2", List.of(new Destroyer()));
    }

    @Test
    void testShootAtHitAndMiss() {
        String result = player.shootAt(p1, enemy);
        assertTrue(result.equals("HIT") || result.startsWith("SUNK"));
        String miss = player.shootAt(new Position(5, 5), enemy);
        assertEquals("MISS", miss);
    }

    @Test
    void testHasLost() {
        Ship s = new Destroyer();
        s.setPositions(List.of(p1, p2));
        Player testPlayer = new Player("Enemy", List.of(s));

        assertFalse(testPlayer.hasLost());
        s.registerHit(p1);
        s.registerHit(p2);
        assertTrue(testPlayer.hasLost());
    }
}
