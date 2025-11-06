package battleship.swing;

import battleship.Table;
import battleship.Position;
import battleship.ship.Battleship;
import battleship.Ship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardPanelTest {

    private Table table;
    private List<Ship> ships;
    private PlayerBoardPanel panel;
    private final int cellSize = 40;

    @BeforeEach
    void setUp() {
        table = new Table();
        ships = new ArrayList<>();

        // 🔹 Un solo barco horizontal en fila 0
        Battleship b1 = new Battleship();
        b1.setPositions(Arrays.asList(
                new Position(0, 0),
                new Position(1, 0),
                new Position(2, 0),
                new Position(3, 0)
        ));
        ships.add(b1);

        // Pinta el barco en la matriz del jugador
        table.drowMyPlayerTable(ships);

        panel = new PlayerBoardPanel(table);
    }

    @Test
    void testPaintComponent_PaintsSingleShipAndWaterCorrectly() {
        BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        panel.paintComponent(g);

        int grayRGB = Color.GRAY.getRGB();
        int cyanRGB = Color.CYAN.getRGB();

        // Celdas con barco
        int[][] shipPositions = {
                {0, 0}, {1, 0}, {2, 0}, {3, 0}
        };

        for (int[] pos : shipPositions) {
            int x = pos[0] * cellSize + cellSize / 2;
            int y = pos[1] * cellSize + cellSize / 2;
            int pixelColor = img.getRGB(x, y);
            assertEquals(grayRGB, pixelColor);
        }

        // Celda sin barco
        int waterX = 9 * cellSize + cellSize / 2;
        int waterY = 9 * cellSize + cellSize / 2;
        int waterPixel = img.getRGB(waterX, waterY);
        assertEquals(cyanRGB, waterPixel);
    }
}
