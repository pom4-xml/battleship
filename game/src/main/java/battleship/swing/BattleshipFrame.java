package battleship.swing;

import javax.swing.*;

import battleship.Table;


public class BattleshipFrame extends JFrame {
    private BoardPanel boardPanel;
    private int cellSize = 40;

    public BattleshipFrame(Table table) {
        boardPanel = new BoardPanel(table);
        add(boardPanel);

        setTitle("Battleship Frame");
        
        setSize(10 * cellSize, 10 * cellSize);      
        setLocation(500, 500);     

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Método para actualizar la vista cuando llegue un nuevo comando
    public void refreshBoard() {
        boardPanel.repaint();
    }
}
