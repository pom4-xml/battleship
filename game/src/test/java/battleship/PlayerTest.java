package battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import battleship.ship.*;

import java.util.ArrayList;

class PlayerTest {

    private Player player1;
    private Player player2;
    private Destroyer destroyer;
    private Submarine submarine;
    private Position d1, d2, s1, s2, s3;

    @BeforeEach
    void setUp() {
        destroyer = new Destroyer();
        submarine = new Submarine();

        ArrayList<Ship> ships1 = new ArrayList<>();
        ships1.add(destroyer);
        ships1.add(submarine);

        ArrayList<Ship> ships2 = new ArrayList<>();
        ships2.add(new Destroyer());
        ships2.add(new Submarine());

        player1 = new Player("Alice", ships1);
        player2 = new Player("Bob", ships2);

        d1 = new Position(0, 0);
        d2 = new Position(0, 1);
        ArrayList<Position> destroyerPositions = new ArrayList<>();
        destroyerPositions.add(d1);
        destroyerPositions.add(d2);
        destroyer.setPositions(destroyerPositions);

        s1 = new Position(1, 0);
        s2 = new Position(1, 1);
        s3 = new Position(1, 2);
        ArrayList<Position> submarinePositions = new ArrayList<>();
        submarinePositions.add(s1);
        submarinePositions.add(s2);
        submarinePositions.add(s3);
        submarine.setPositions(submarinePositions);
    }

    @Test
    void testGetName() {
        assertEquals("Alice", player1.getName());
    }

    @Test
    void testHasLost() {
        assertFalse(player1.hasLost());
        destroyer.registerHit(d1);
        destroyer.registerHit(d2);
        assertFalse(player1.hasLost());
        submarine.registerHit(s1);
        submarine.registerHit(s2);
        submarine.registerHit(s3);
        assertTrue(player1.hasLost());
    }

    @Test
    void testShootAt() {
        Position shotHit = new Position(0, 0);
        Position shotMiss = new Position(5, 5);
        String result1 = player1.shootAt(shotHit, player2);
        assertTrue(result1.equals("HIT") || result1.startsWith("SUNK"));
        String result2 = player1.shootAt(shotMiss, player2);
        assertEquals("MISS", result2);
    }
}
