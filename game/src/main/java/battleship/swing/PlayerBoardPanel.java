package battleship.swing;

import javax.swing.*;

import battleship.logic.Table;

import java.awt.*;

public class PlayerBoardPanel extends JPanel {
    private transient Table table;
    private int cellSize = 40;

    public PlayerBoardPanel(Table table) {
        this.table = table;
        setPreferredSize(new Dimension(10 * cellSize, 10 * cellSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[][] matrix = table.getMyPlayerMatrix();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int value = matrix[x][y];
                if (value == 1) g.setColor(Color.GRAY);  // barco
                else g.setColor(Color.CYAN);             // agua

                g.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);

                g.setColor(Color.BLACK);
                g.drawRect(y * cellSize, x * cellSize, cellSize, cellSize);
            }
        }
    }
}

