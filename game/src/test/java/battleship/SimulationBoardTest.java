package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import battleship.logic.Position;
import battleship.logic.Ship;
import battleship.logic.SimulationBoard;
import battleship.logic.ship.Destroyer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SimulationBoardTest {

    private SimulationBoard board;
    private Ship ship;
    private Position p1, p2, p3;

    @BeforeEach
    void setUp() {
        ship = new Destroyer();
        p1 = new Position(0, 0);
        p2 = new Position(0, 1);
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(p1);
        positions.add(p2);
        ship.setPositions(positions);

        ArrayList<Ship> ships = new ArrayList<>();
        ships.add(ship);
        board = new SimulationBoard(ships);

        p3 = new Position(1, 1);
    }

    @Test
    void testProcessShotHit() {
        String result = board.processShot(p1);
        assertEquals("HIT", result);
        assertTrue(ship.getHits().contains(p1));
    }

    @Test
    void testProcessShotSunk() {
        board.processShot(p1);
        String result = board.processShot(p2);
        assertEquals("SUNK", result);
        assertTrue(ship.isSunk());
    }

    @Test
    void testProcessShotMiss() {
        String result = board.processShot(p3);
        assertEquals("MISS", result);
    }

    @Test
    void testHasShipsRemaining() {
        assertTrue(board.hasShipsRemaining());
        board.processShot(p1);
        board.processShot(p2);
        assertFalse(board.hasShipsRemaining());
    }
}