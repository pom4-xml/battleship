package battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import battleship.ship.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class PlayerTest {

    private Player player1;
    private Player player2;
    private Destroyer destroyer;
    private Submarine submarine;
    private Battleship battleship;
    private Position d1, d2, s1, s2, s3;

    @BeforeEach
    void setUp() {
        destroyer = new Destroyer();
        submarine = new Submarine();
        battleship = new Battleship();

        ArrayList<Ship> ships1 = new ArrayList<>();
        ships1.add(destroyer);
        ships1.add(submarine);

        ArrayList<Ship> ships2 = new ArrayList<>();
        ships2.add(battleship);

        player1 = new Player("Alice", ships1);
        player2 = new Player("Bob", ships2);

        // Destroyer en fila 0, columnas 0-1
        d1 = new Position(0, 0);
        d2 = new Position(0, 1);
        ArrayList<Position> destroyerPositions = new ArrayList<>();
        destroyerPositions.add(d1);
        destroyerPositions.add(d2);
        destroyer.setPositions(destroyerPositions);

        // Submarine en fila 1, columnas 0-2
        s1 = new Position(1, 0);
        s2 = new Position(1, 1);
        s3 = new Position(1, 2);
        ArrayList<Position> submarinePositions = new ArrayList<>();
        submarinePositions.add(s1);
        submarinePositions.add(s2);
        submarinePositions.add(s3);
        submarine.setPositions(submarinePositions);

        // Battleship en fila 5, columnas 0-3
        ArrayList<Position> battleshipPositions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            battleshipPositions.add(new Position(5, i));
        }
        battleship.setPositions(battleshipPositions);
    }

    @Test
    void testGetName() {
        assertEquals("Alice", player1.getName());
        assertEquals("Bob", player2.getName());
    }

    @Test
    void testGetShips() {
        List<Ship> ships = player1.getShips();
        assertNotNull(ships);
        assertEquals(2, ships.size());
        assertTrue(ships.contains(destroyer));
        assertTrue(ships.contains(submarine));
    }

    @Test
    void testHasLost_InitiallyFalse() {
        assertFalse(player1.hasLost());
        assertFalse(player2.hasLost());
    }

    @Test
    void testHasLost_OneShipSunk() {
        destroyer.registerHit(d1);
        destroyer.registerHit(d2);
        assertTrue(destroyer.isSunk());
        assertFalse(player1.hasLost()); // Submarine still alive
    }

    @Test
    void testHasLost_AllShipsSunk() {
        // Sink destroyer
        destroyer.registerHit(d1);
        destroyer.registerHit(d2);
        
        // Sink submarine
        submarine.registerHit(s1);
        submarine.registerHit(s2);
        submarine.registerHit(s3);
        
        assertTrue(player1.hasLost());
    }

    @Test
    void testShootAt_Hit() {
        String result = player1.shootAt(new Position(5, 0), player2);
        assertEquals("HIT", result);
        assertTrue(battleship.getHits().contains(new Position(5, 0)));
    }

    @Test
    void testShootAt_Miss() {
        String result = player1.shootAt(new Position(9, 9), player2);
        assertEquals("MISS", result);
    }

    @Test
    void testShootAt_Sunk() {
        // Hit all destroyer positions
        player2.shootAt(d1, player1);
        String result = player2.shootAt(d2, player1);
        
        assertEquals("SUNK Destroyer", result);
        assertTrue(destroyer.isSunk());
    }

    @Test
    void testShootAt_MultipleHits() {
        player1.shootAt(new Position(5, 0), player2);
        player1.shootAt(new Position(5, 1), player2);
        player1.shootAt(new Position(5, 2), player2);
        
        assertEquals(3, battleship.getHits().size());
        assertFalse(battleship.isSunk());
        
        String result = player1.shootAt(new Position(5, 3), player2);
        assertEquals("SUNK Battleship", result);
        assertTrue(battleship.isSunk());
    }

    @Test
    void testShootAt_SamePositionTwice() {
        String result1 = player1.shootAt(new Position(5, 0), player2);
        String result2 = player1.shootAt(new Position(5, 0), player2);
        
        assertEquals("HIT", result1);
        assertEquals("HIT", result2); // Still registers as hit
    }

    @Test
    void testPlaceShipsManually_ValidInput() {
        String input = "0\n0\ntrue\n" +  // Destroyer: row 0, col 0, horizontal
                       "2\n0\nfalse\n";   // Submarine: row 2, col 0, vertical
        
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        Player player = new Player("TestPlayer", List.of(
            new Destroyer(), 
            new Submarine()
        ));
        
        player.placeShipsManually(scanner);
        
        // Verify destroyer placement
        Ship destroyer = player.getShips().get(0);
        assertNotNull(destroyer.getPositions());
        assertEquals(2, destroyer.getPositions().size());
        
        // Verify submarine placement
        Ship submarine = player.getShips().get(1);
        assertNotNull(submarine.getPositions());
        assertEquals(3, submarine.getPositions().size());
    }

    @Test
    void testPlaceShipsManually_OverlappingThenValid() {
        // Place two destroyers
        String input = "0\n0\ntrue\n" +   // First destroyer: row 0, col 0-1
                       "0\n0\ntrue\n" +   // Try same position (invalid)
                       "1\n0\ntrue\n";    // Valid position
        
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        Player player = new Player("TestPlayer", List.of(
            new Destroyer(),
            new Destroyer()
        ));
        
        player.placeShipsManually(scanner);
        
        Ship destroyer1 = player.getShips().get(0);
        Ship destroyer2 = player.getShips().get(1);
        
        assertEquals(2, destroyer1.getPositions().size());
        assertEquals(2, destroyer2.getPositions().size());
        
        // Verify no overlap
        for (Position p : destroyer1.getPositions()) {
            assertFalse(destroyer2.getPositions().contains(p));
        }
    }

    @Test
    void testPlayerWithNoShips() {
        Player emptyPlayer = new Player("Empty", new ArrayList<>());
        assertTrue(emptyPlayer.hasLost()); // No ships = already lost
    }

    @Test
    void testShootAt_EmptyEnemy() {
        Player emptyPlayer = new Player("Empty", new ArrayList<>());
        String result = player1.shootAt(new Position(0, 0), emptyPlayer);
        assertEquals("MISS", result);
    }

    @Test
    void testGetShipsReturnsOriginalList() {
        List<Ship> ships = player1.getShips();
        ships.clear();
        // Verify original list is modified (no defensive copy)
        assertEquals(0, player1.getShips().size());
    }

    @Test
    void testMultiplePlayersIndependent() {
        assertFalse(player1.hasLost());
        assertFalse(player2.hasLost());
        
        // Sink all player2's ships
        for (Position p : battleship.getPositions()) {
            player1.shootAt(p, player2);
        }
        
        assertTrue(player2.hasLost());
        assertFalse(player1.hasLost()); // player1 still has ships
    }
}