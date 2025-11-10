package battleship.logic;

import java.util.ArrayList;
import java.util.List;

import battleship.logic.ship.Battleship;
import battleship.logic.ship.Carrier;
import battleship.logic.ship.Cruiser;
import battleship.logic.ship.Destroyer;
import battleship.logic.ship.Submarine;

public class BattleshipManager {
    
    private BattleshipManager() {
    }

    public static List<Ship> createStandardShips() {
        List<Ship> ships = new ArrayList<>();

        ships.add(new Destroyer());
        ships.add(new Submarine());
        ships.add(new Cruiser());
        ships.add(new Battleship());
        ships.add(new Carrier());

        return ships;
    }
}