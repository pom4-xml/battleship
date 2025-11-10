package battleship.swing;

import battleship.logic.Position;
import battleship.logic.Ship;
import battleship.logic.Table;
import battleship.logic.ship.Battleship;

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
                new Position(0, 1),
                new Position(0, 2),
                new Position(0, 3)
        ));
        ships.add(b1);

        // Pinta el barco en la matriz del jugador
        table.drowMyPlayerTable(ships);

        panel = new PlayerBoardPanel(table);
        // ✅ Fijar tamaño del panel antes de pintar
        panel.setSize(cellSize * 10, cellSize * 10);
    }

    @Test
    void testPaintComponent_PaintsSingleShipAndWaterCorrectly() {
        BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        // ✅ Pintar el panel en la imagen
        panel.paint(g);

        int grayRGB = Color.GRAY.getRGB();
        int cyanRGB = Color.CYAN.getRGB();

        // Celdas con barco (fila 0, columnas 0-3)
        // En PlayerBoardPanel: g.fillRect(y * cellSize, x * cellSize, ...)
        // Entonces para fila=0, columna=0: fillRect(0, 0, 40, 40)
        int[][] shipPositions = {
                {0, 0}, {1, 0}, {2, 0}, {3, 0}  // (columna, fila)
        };

        for (int[] pos : shipPositions) {
            int col = pos[0];
            int row = pos[1];
            // En la imagen: x = col * cellSize, y = row * cellSize
            int x = col * cellSize + cellSize / 2;
            int y = row * cellSize + cellSize / 2;
            int pixelColor = img.getRGB(x, y);
            assertEquals(grayRGB, pixelColor, 
                "Expected GRAY at (" + x + ", " + y + ") for ship position [" + col + ", " + row + "]");
        }

        // Celda sin barco (última celda: fila 9, columna 9)
        int waterX = 9 * cellSize + cellSize / 2;
        int waterY = 9 * cellSize + cellSize / 2;
        int waterPixel = img.getRGB(waterX, waterY);
        assertEquals(cyanRGB, waterPixel, 
            "Expected CYAN at (" + waterX + ", " + waterY + ") for water");
    }
}
