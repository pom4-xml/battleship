package battleship.swing;

import javax.swing.*;

import battleship.logic.Table;

import java.awt.Dimension;
import java.awt.GridLayout;


public class BattleshipFrame extends JFrame {
    private BoardPanel rivalPanel;
    private PlayerBoardPanel playerPanel;
    private int cellSize = 40;

    public BattleshipFrame(String playerName, Table table, Table enemyTabñe) {
        setTitle(playerName + "'s Battleship Frame");
        JPanel container = new JPanel(new GridLayout(1, 2));
        container.setPreferredSize(new Dimension(2 * 10 * cellSize, 10 * cellSize));
        playerPanel = new PlayerBoardPanel(table);
        rivalPanel = new BoardPanel(enemyTabñe);
        container.add(playerPanel);
        container.add(rivalPanel);
        setContentPane(container);
        
        setSize(2 * 10 * cellSize, 10 * cellSize + cellSize);    //margen  
        setLocation(500, 500);     

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Método para actualizar la vista cuando llegue un nuevo comando
    public void refreshBoard() {
        rivalPanel.repaint();
        playerPanel.repaint();
    }
}
