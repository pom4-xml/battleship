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
@Test
void testHasLostWithSomeShipsSunk() {
    // Hundimos el primer barco del jugador2
    Ship firstShip = player2.getShips().get(0);
    for (Position pos : firstShip.getPositions()) {
        player1.shootAt(pos, player2);
    }
    // No todos están hundidos
    assertFalse(player2.hasLost());
}



@Test
void testGetters() {
    assertEquals("Player1", player1.getName());
    assertEquals(player1.getShips(), player1.getShips());
}



@Test
void testShootAtHitsSecondShip() {
    Ship secondShip = player2.getShips().get(1);
    Position pos = secondShip.getPositions().get(0);
    String result = player1.shootAt(pos, player2);
    assertEquals("HIT", result);
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

        // Validamos tamaños
        assertEquals(2, ships.get(0).getPositions().size());
        assertEquals(3, ships.get(1).getPositions().size());
        assertEquals(3, ships.get(2).getPositions().size());
        assertEquals(4, ships.get(3).getPositions().size());
        assertEquals(5, ships.get(4).getPositions().size());

        // Validamos que no hay superposición
        for (int i = 0; i < ships.size(); i++) {
            for (int j = i + 1; j < ships.size(); j++) {
                for (Position pos : ships.get(i).getPositions()) {
                    assertFalse(ships.get(j).getPositions().contains(pos),
                            "Barcos se superponen en " + pos);
                }
            }
        }

        sc.close();
    }

    @Test
    void testPlaceShipsManuallyWithOverlap() {
        String inputData =
                "0 0 true\n" + // Destroyer
                "0 0 true\n" + // Submarine (intento inválido, superpone)
                "1 0 true\n" + // Submarine válido
                "2 0 true\n" + // Cruiser
                "3 0 true\n" + // Battleship
                "4 0 true\n";  // Carrier
        Scanner sc = new Scanner(new ByteArrayInputStream(inputData.getBytes()));

        Player p = new Player("Tester", BattleshipManager.createStandardShips());
        p.placeShipsManually(sc);

        List<Ship> ships = p.getShips();
        assertEquals(2, ships.get(0).getPositions().size());
        assertEquals(3, ships.get(1).getPositions().size());

        sc.close();
    }

}
