package battleship;

import java.util.List;

public class Player {
    String name;
    List<Ship> listShip;

    public Player(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



 

    public List<Ship> getListShip() {
        return listShip;
    }

    public void setListShip(List<Ship> listShip) {
        this.listShip = listShip;
    }
    
    

}
