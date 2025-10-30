package battleship;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import battleship.ship.Battleship;

class TableTest {
    
    Table table;
    List<Ship> myListOfShips;
    
    @BeforeEach
    void setup(){
        table = new Table();
        myListOfShips = new ArrayList<>();
        Ship ship = new Battleship();
        Position p1 = new Position(0, 0);
        Position p2 = new Position(1, 0);
        Position p3 = new Position(2, 0);
        Position p4 = new Position(3, 0);
        List<Position> lPositions = new ArrayList<>();
        lPositions.add(p1);
        lPositions.add(p2);
        lPositions.add(p3);
        lPositions.add(p4);
        ship.setPositions(lPositions);

        myListOfShips.add(ship);
    }

    @Test
    void checkRivalShot(){
        boolean result1 = table.checkRivalShot(new Position(0, 0), myListOfShips);
        assertTrue(result1);
        boolean result2 = table.checkRivalShot(new Position(0, 1), myListOfShips);
        assertFalse(result2);
    }

    @Test
    void checkRivalShot_PosNull(){
        Position position = null;
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            ()-> table.checkRivalShot(position, myListOfShips));
        assertEquals("Postition cant't be null",ex.getMessage());

    }

    @Test
    void checkRivalShot_ShipListNull(){
        List<Ship> nullList = null;
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            ()-> table.checkRivalShot(new Position(0, 0), nullList));
        assertEquals("List of Ships can't be null",ex.getMessage());

    }

    @Test
    void checkRivalShot_ShipListIsEmpty(){
        List<Ship> emptyList = new ArrayList<>();
        IllegalStateException ex = assertThrows(
             IllegalStateException.class,
            ()-> table.checkRivalShot(new Position(0, 0), emptyList));
        assertEquals("List of ships can't be empty",ex.getMessage());

    }

    @Test
    void getterMatrix(){
        int [][] matriz = table.getMatrix();
        assertEquals(0,matriz[0][0]);
    }




}
