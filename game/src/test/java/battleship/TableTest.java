package battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import battleship.ship.Battleship;

class TableTest {

    private Table table;
    private List<Ship> emptyShips;
    private Ship ship;

    @BeforeEach
    void setup() {
        table = new Table();
        emptyShips = new ArrayList<>();

        ship = new Battleship();
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(1, 0));
        positions.add(new Position(2, 0));
        positions.add(new Position(3, 0));
        ship.setPositions(positions);

        emptyShips.add(ship);
    }

    @Test
    void testShotHit() {
        boolean result = table.checkRivalShot(new Position(0, 0), emptyShips);
        assertTrue(result);
        assertEquals(2, table.getMatrix()[0][0]);
    }

    @Test
    void testShotMiss() {
        boolean result = table.checkRivalShot(new Position(0, 1), emptyShips);
        assertFalse(result);
        assertEquals(1, table.getMatrix()[0][1]);
    }

    @Test
    void testShotPositionNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> table.checkRivalShot(null, emptyShips));
        assertEquals("Postition cant't be null", ex.getMessage());
    }

    @Test
    void testShotShipsListNull() {
        Position position = new Position(0, 0);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> table.checkRivalShot(position, null));

        assertEquals("List of Ships can't be null", ex.getMessage());
    }

    @Test
    void testShotShipsListEmpty() {
        Position position = new Position(0, 0);
        List<Ship> ships = new ArrayList<>();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> table.checkRivalShot(position, ships));

        assertEquals("List of ships can't be empty", ex.getMessage());
    }

    @Test
    void testGetMatrixInitial() {
        int[][] matrix = table.getMatrix();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(0, matrix[i][j]);
            }
        }
    }

    // my ships

    @Test
    void testDrawMyPlayerTable_CorrectPositions() {
        table.drowMyPlayerTable(emptyShips);
        int[][] myMatrix = table.getMyPlayerMatrix();

        for (Position p : ship.getPositions()) {
            assertEquals(1, myMatrix[p.getX()][p.getY()],
                    String.format("Expected ship at (%d,%d)", p.getX(), p.getY()));
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!ship.getPositions().contains(new Position(i, j))) {
                    assertEquals(0, myMatrix[i][j],
                            String.format("Expected water at (%d,%d)", i, j));
                }
            }
        }
    }

    @Test
    void testDrawMyPlayerTable_NullListThrows() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> table.drowMyPlayerTable(null));
        assertEquals("List ships can't be null", ex.getMessage());
    }

    @Test
    void testDrawMyPlayerTable_EmptyListThrows() {
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> table.drowMyPlayerTable(new ArrayList<>()));
        assertEquals("List of ships can't be empty", ex.getMessage());
    }

    @Test
    void testDrawMyPlayerTable_MatrixNotCleanThrows() {
        table.getMyPlayerMatrix()[0][0] = 1; // simula matriz no vacía
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> table.drowMyPlayerTable(emptyShips));
        assertEquals("Matrix must be clean", ex.getMessage());
    }

    @Test
    void testDrawMyPlayerTable_PositionOutOfBoundsThrows() throws Exception {
        Position invalidPosition = new Position(0, 0);

        java.lang.reflect.Field fieldX = Position.class.getDeclaredField("x");
        java.lang.reflect.Field fieldY = Position.class.getDeclaredField("y");
        fieldX.setAccessible(true);
        fieldY.setAccessible(true);

        fieldX.setInt(invalidPosition, 11);
        fieldY.setInt(invalidPosition, 0);

        Ship invalidShip = new Battleship();
        invalidShip.setPositions(java.util.Arrays.asList(invalidPosition));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> table.drowMyPlayerTable(java.util.Collections.singletonList(invalidShip)));

        assertTrue(ex.getMessage().contains("Position out of bounds"));
    }

    //poner en ammirllo ->

    @Test
    void testShipCompletelySunk() {
        // Marcar todas las posicionescomo impactadas 
        for (Position p : ship.getPositions()) {
            table.getMatrix()[p.getX()][p.getY()] = 2;
        }

        boolean result = table.checkIfSunk(ship, table.getMatrix());

        assertTrue(result);

        // Verificar que todas las posiciones ahora sean 3 (amarillo)
        for (Position p : ship.getPositions()) {
            assertEquals(3, table.getMatrix()[p.getX()][p.getY()]);
        }
    }

    @Test
    void testShipPartiallyHit() {
        // Marcar solo algunas posiciones como impactadas
        table.getMatrix()[0][0] = 2;
        table.getMatrix()[1][0] = 0; // no tocado
        table.getMatrix()[2][0] = 2;
        table.getMatrix()[3][0] = 0; // no tocado

        boolean result = table.checkIfSunk(ship, table.getMatrix());

        assertFalse(result);

        // Verificar que las posiciones no tocadas no se han cambiado a 3
        assertEquals(2, table.getMatrix()[0][0]);
        assertEquals(0, table.getMatrix()[1][0]);
        assertEquals(2, table.getMatrix()[2][0]);
        assertEquals(0, table.getMatrix()[3][0]);
    }

    @Test
    void testShipNotHit() {
        // Ninguna posición impactada
        boolean result = table.checkIfSunk(ship, table.getMatrix());

        assertFalse(result);

        // Todas las posiciones deberían seguir 0
        for (Position p : ship.getPositions()) {
            assertEquals(0, table.getMatrix()[p.getX()][p.getY()]);
        }
    }





}
