package battleship.swing;

import battleship.Table;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

class BattleshipFrameTest extends EasyMockSupport {

    private Table table;
    private BattleshipFrame frame;
    private int cellSize = 40;

    @BeforeEach
    void setUp() {
        table = new Table();
        frame = new BattleshipFrame(table);
    }

    @Test
    void testFrameInitialization() {
        //Frame not null
        assertNotNull(frame);

        // Ver su el panel se ha cargado
        assertTrue(frame.getContentPane().getComponentCount() > 0);
        assertTrue(frame.getContentPane().getComponent(0) instanceof BoardPanel);

        // Ver si board panel tiene table
        BoardPanel panel = (BoardPanel) frame.getContentPane().getComponent(0);
        assertEquals(table, panel.getTable());
    }

    @Test
    void testRefreshBoard_CallsRepaint() throws Exception{
        BoardPanel mockPanel = createMock(BoardPanel.class);
        mockPanel.repaint();
        EasyMock.expectLastCall().once();

        replayAll();

        Field field = BattleshipFrame.class.getDeclaredField("boardPanel");
        field.setAccessible(true);
        field.set(frame, mockPanel);

        frame.refreshBoard();

        verifyAll();
    }

    @Test
    void testFrameProperties() {
        assertEquals("Battleship Frame", frame.getTitle());
        assertEquals(WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
        assertTrue(frame.isVisible());

        assertEquals(10*cellSize, frame.getWidth());
        assertEquals(10*cellSize, frame.getHeight());
        assertEquals(500, frame.getX());
        assertEquals(500, frame.getY());
    }
}
