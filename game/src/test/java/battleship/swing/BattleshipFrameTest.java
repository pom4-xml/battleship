package battleship.swing;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import battleship.logic.Table;

import java.lang.reflect.Field;

class BattleshipFrameTest extends EasyMockSupport {

    Table playerTable;
    Table enemyTable;
    private BattleshipFrame frame;
    private int cellSize = 40;

    @BeforeEach
    void setUp() {
        playerTable = new Table();
        enemyTable = new Table();
        frame = new BattleshipFrame("TestPlayer", playerTable, enemyTable);
    }

    @Test
    void testFrameInitialization() {
        assertNotNull(frame, "Frame should not be null");

        // Contenedor principal
        JPanel contentPane = (JPanel) frame.getContentPane();
        assertTrue(contentPane.getComponentCount() > 0);

        // Sub paneles
        assertEquals(2, contentPane.getComponentCount());
        assertTrue(contentPane.getComponent(0) instanceof PlayerBoardPanel);
        assertTrue(contentPane.getComponent(1) instanceof BoardPanel);
    }

    @Test
    void testRefreshBoard_CallsRepaint() throws Exception {
        BoardPanel mockRivalPanel = createMock(BoardPanel.class);
        PlayerBoardPanel mockPlayerPanel = createMock(PlayerBoardPanel.class);

        mockRivalPanel.repaint();
        EasyMock.expectLastCall().once();

        mockPlayerPanel.repaint();
        EasyMock.expectLastCall().once();

        replayAll();

        Field rivalField = BattleshipFrame.class.getDeclaredField("rivalPanel");
        rivalField.setAccessible(true);
        rivalField.set(frame, mockRivalPanel);

        Field playerField = BattleshipFrame.class.getDeclaredField("playerPanel");
        playerField.setAccessible(true);
        playerField.set(frame, mockPlayerPanel);

        frame.refreshBoard();

        verifyAll();
    }

    @Test
    void testFrameProperties() {
        assertEquals("Battleship Frame", frame.getTitle());
        assertEquals(WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
        assertTrue(frame.isVisible());

        int expectedWidth = 2 * 10 * cellSize;
        int expectedHeight = 10 * cellSize + cellSize; // +cellSize margen extra

        assertEquals(expectedWidth, frame.getWidth());
        assertEquals(expectedHeight, frame.getHeight());

        // Posición fija establecida en el constructor
        assertEquals(500, frame.getX());
        assertEquals(500, frame.getY());
    }
}
