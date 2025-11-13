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

    private Table playerTable;
    private Table enemyTable;
    private BattleshipFrame frame;
    private final int cellSize = 40;
    private final String playerName = "TestPlayer";

    @BeforeEach
    void setUp() {
        playerTable = new Table();
        enemyTable = new Table();
        frame = new BattleshipFrame(playerName, playerTable, enemyTable);
        frame.setVisible(false);
    }

    @Test
    void testFrameInitialization() {
        assertNotNull(frame, "Frame should not be null");

        JPanel contentPane = (JPanel) frame.getContentPane();
        assertNotNull(contentPane);
        assertEquals(2, contentPane.getComponentCount(), "Should have exactly 2 panels");

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
        assertEquals(playerName + "'s Battleship Frame", frame.getTitle());
        assertEquals(WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());

        int expectedWidth = 2 * 10 * cellSize;
        int expectedHeight = 10 * cellSize + cellSize;

        assertEquals(expectedWidth, frame.getWidth());
        assertEquals(expectedHeight, frame.getHeight());

        assertEquals(500, frame.getX());
        assertEquals(500, frame.getY());
    }
}