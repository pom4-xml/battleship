package battleship.swing;


import javax.swing.*;

import battleship.Table;

import java.awt.*;

public class BoardPanel extends JPanel {
    private transient Table table;//sonar qube
    private int cellSize = 40;

    public BoardPanel(Table table) {
        this.table = table;
        setPreferredSize(new Dimension(10 * cellSize, 10 * cellSize));
    }

    public Table getTable() {
        return table;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[][] matrix = table.getMatrix();

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int value = matrix[x][y];
                switch (value) {
                    case 0 : g.setColor(Color.BLUE); break;  // agua
                    case 1 : g.setColor(Color.BLACK); break;  // disparo fallido
                    case 2 : g.setColor(Color.RED);  break;  // impacto
                    default: break;
                }
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }
}
