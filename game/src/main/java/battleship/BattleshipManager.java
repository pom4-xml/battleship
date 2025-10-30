package battleship;

import java.util.ArrayList;
import java.util.List;

import battleship.ship.Battleship;
import battleship.ship.Carrier;
import battleship.ship.Cruiser;
import battleship.ship.Destroyer;
import battleship.ship.Submarine;

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
