package battleship;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class SimulationGameControllerTest extends EasyMockSupport {

    @Test
    public void testStartGameCalled() throws Exception {
        SimulationGameController mockController = createMock(SimulationGameController.class);

        mockController.startGame();
        EasyMock.expectLastCall().once();

        replayAll();

        SimulationMain main = new SimulationMain();

        Field controllerField = SimulationMain.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(main, mockController);

        main.run();

        verifyAll();
    }
}
