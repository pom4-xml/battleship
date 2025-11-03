package battleship.swing;


import battleship.Table;
import battleship.Position;
import battleship.Battleship;
import battleship.Ship;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




import java.awt.Graphics;
import java.awt.image.BufferedImage;



class BoardPanelTest {

    private Table table;
    private List<Ship> ships;
    private BoardPanel panel;

    @BeforeEach
    void setUp() {
        table = new Table();
        ships = new ArrayList<>();

        Battleship b1 = new Battleship();
        b1.setPositions(Arrays.asList(
                new Position(0,0),
                new Position(1,0),
                new Position(2,0),
                new Position(3,0)
        ));
        ships.add(b1);

        Battleship b2 = new Battleship();
        b2.setPositions(Arrays.asList(
                new Position(5,5),
                new Position(5,6),
                new Position(5,7),
                new Position(5,8)
        ));
        ships.add(b2);

        panel = new BoardPanel(table);
    }

    @Test
    void testPaintComponent() {
        table.checkRivalShot(new Position(0,0), ships); // impacto
        table.checkRivalShot(new Position(4,4), ships); // fallo

        // Crear un BufferedImage para pasarle Graphics a paintComponent
        BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        // Llamar directamente al paintComponent
        panel.paintComponent(g);

        assertEquals(2, table.getMatrix()[0][0]); // impacto
        assertEquals(1, table.getMatrix()[4][4]); // fallo
    }
}



