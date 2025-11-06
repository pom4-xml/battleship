package battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimulationMainTest {

    @Test
    void testMain() {
        assertDoesNotThrow(() -> {
            Thread testThread = new Thread(() -> SimulationMain.main(new String[]{}));
            testThread.start();
            testThread.interrupt();
        });
    }
}