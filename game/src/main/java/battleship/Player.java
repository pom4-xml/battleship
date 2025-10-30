package battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import battleship.ship.*;

import java.util.ArrayList;

class PlayerTest {

    private Player player1;
    private Player player2;
    private Ship destroyer;
    private Ship submarine;
    private Position p1, p2;

    @BeforeEach
    void setUp() {
        destroyer = new Destroyer();
        submarine = new Submarine();

        ArrayList<Ship> ships1 = new ArrayList<Ship>();
        ships1.add(destroyer);
        ships1.add(submarine);

        ArrayList<Ship> ships2 = new ArrayList<Ship>();
        ships2.add(new Destroyer());
        ships2.add(new Submarine());

        player1 = new Player("Alice", ships1);
        player2 = new Player("Bob", ships2);

        // Set positions manually
        p1 = new Position(0, 0);
        p2 = new Position(0, 1);
        ArrayList<Position> destroyerPositions = new ArrayList<Position>();
        destroyerPositions.add(p1);
        destroyerPositions.add(p2);
        destroyer.setPositions(destroyerPositions);
    }

    @Test
    void testGetName() {
        assertEquals("Alice", player1.getName());
    }

    @Test
    void testHasLost() {
        assertFalse(player1.hasLost());
        destroyer.registerHit(p1);
        destroyer.registerHit(p2);
        submarine.setHits(new ArrayList<Position>()); // still alive
        assertFalse(player1.hasLost());
        submarine.registerHit(new Position(1, 0));
        submarine.registerHit(new Position(1, 1));
        submarine.registerHit(new Position(1, 2));
        assertTrue(player1.hasLost());
    }

    @Test
    void testShootAt() {
        Position shot1 = new Position(0, 0);
        Position shot2 = new Position(5, 5);

        String result1 = player1.shootAt(shot1, player2);
        assertTrue(result1.equals("HIT") || result1.startsWith("SUNK"));

        String result2 = player1.shootAt(shot2, player2);
        assertEquals("MISS", result2);
    }
}
