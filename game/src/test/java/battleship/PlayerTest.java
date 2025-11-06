package battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import battleship.ship.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class PlayerTest {

    private Player player1;
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

        player1 = new Player("Alice", ships1);

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
void testGetShips() {
    assertEquals(2, player1.getShips().size());
    assertTrue(player1.getShips().contains(destroyer));
    assertTrue(player1.getShips().contains(submarine));
}

@Test
void testShootAtHitMissSunk() {
    Player player2 = new Player("Bob", new ArrayList<>());
    Destroyer enemyDestroyer = new Destroyer();
    ArrayList<Position> positions = new ArrayList<>();
    positions.add(new Position(0,0));
    positions.add(new Position(0,1));
    enemyDestroyer.setPositions(positions);
    player2.getShips().add(enemyDestroyer);

    // HIT
    String result1 = player1.shootAt(new Position(0,0), player2);
    assertEquals("HIT", result1);

    // SUNK
    String result2 = player1.shootAt(new Position(0,1), player2);
    assertEquals("SUNK Destroyer", result2);

    // MISS
    String result3 = player1.shootAt(new Position(5,5), player2);
    assertEquals("MISS", result3);
}

    @Test
    void testPlaceShipsManually() {
        destroyer = new Destroyer();

        ArrayList<Ship> ships = new ArrayList<>(Arrays.asList(destroyer));

        Player player = new Player("Test", ships);

       
        String input = "0\n0\ntrue\n";
        Scanner sc = new Scanner(new java.io.ByteArrayInputStream(input.getBytes()));

        player.placeShipsManually(sc);

        // Verificar posiciones asignadas
        assertEquals(2, destroyer.getPositions().size());
        assertEquals(0, destroyer.getPositions().get(0).getX());
        assertEquals(0, destroyer.getPositions().get(0).getY());
        assertEquals(0, destroyer.getPositions().get(1).getX());
        assertEquals(1, destroyer.getPositions().get(1).getY());
    }





}