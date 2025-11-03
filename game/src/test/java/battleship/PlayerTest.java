package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player1;
    private Player player2;

    @BeforeEach
    void setup() {
        player1 = new Player("Player1", BattleshipManager.createStandardShips());
        player2 = new Player("Player2", BattleshipManager.createStandardShips());

        // Colocación de barcos de prueba para ambos jugadores
        int row = 0;
        for (Ship s : player1.getShips()) {
            s.setPositions(Ship.generatePositions(row, 0, s.getSize(), true));
            row++;
        }
        row = 5;
        for (Ship s : player2.getShips()) {
            s.setPositions(Ship.generatePositions(row, 0, s.getSize(), true));
            row++;
        }
    }

    // --- Getters y hasLost() ---
    @Test
    void testGetters() {
        assertEquals("Player1", player1.getName());
        assertEquals(player1.getShips(), player1.getShips());
    }

    @Test
    void testHasLostInitially() {
        assertFalse(player1.hasLost());
        assertFalse(player2.hasLost());
    }

    @Test
    void testHasLostAfterSomeShipsSunk() {
        Ship firstShip = player2.getShips().get(0);
        for (Position pos : firstShip.getPositions()) {
            player1.shootAt(pos, player2);
        }
        assertFalse(player2.hasLost()); // No todos hundidos
    }

    @Test
    void testHasLostAfterAllSunk() {
        for (Ship s : player2.getShips()) {
            for (Position pos : s.getPositions()) {
                player1.shootAt(pos, player2);
            }
        }
        assertTrue(player2.hasLost());
        assertFalse(player1.hasLost());
    }

    // --- shootAt() ---
    @Test
    void testShootAtHit() {
        Ship secondShip = player2.getShips().get(1);
        Position pos = secondShip.getPositions().get(0);
        String result = player1.shootAt(pos, player2);
        assertEquals("HIT", result);
    }

    @Test
    void testShootAtSunk() {
        Ship firstShip = player2.getShips().get(0);
        for (int i = 0; i < firstShip.getPositions().size(); i++) {
            Position pos = firstShip.getPositions().get(i);
            String result = player1.shootAt(pos, player2);
            if (i == firstShip.getPositions().size() - 1) {
                assertTrue(result.startsWith("SUNK"));
            }
        }
    }

    @Test
    void testShootAtMiss() {
        Position empty = new Position(9, 9);
        String result = player1.shootAt(empty, player2);
        assertEquals("MISS", result);
    }

    // --- placeShipsManually() ---
    @Test
    void testPlaceShipsManuallyValid() {
        String inputData =
                "0 0 true\n" + // Destroyer
                "1 0 true\n" + // Submarine
                "2 0 true\n" + // Cruiser
                "3 0 true\n" + // Battleship
                "4 0 true\n";  // Carrier
        Scanner sc = new Scanner(new ByteArrayInputStream(inputData.getBytes()));

        Player p = new Player("Tester", BattleshipManager.createStandardShips());
        p.placeShipsManually(sc);

        List<Ship> ships = p.getShips();
        assertEquals(2, ships.get(0).getPositions().size());
        assertEquals(3, ships.get(1).getPositions().size());
        assertEquals(3, ships.get(2).getPositions().size());
        assertEquals(4, ships.get(3).getPositions().size());
        assertEquals(5, ships.get(4).getPositions().size());

        sc.close();
    }

    /*@Test
    void testShootAt() {
        Position shotHit = new Position(0, 0);
        Position shotMiss = new Position(5, 5);
        String result1 = player1.shootAt(shotHit, player2);
        assertTrue(result1.equals("HIT") || result1.startsWith("SUNK"));
        String result2 = player1.shootAt(shotMiss, player2);
        assertEquals("MISS", result2);
    }*/
}
