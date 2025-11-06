package battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationMainTest {

    @Test
    void testMainRunsWithoutException() {
        assertDoesNotThrow(() -> SimulationMain.main(new String[]{}),
                "The main method should run without throwing exceptions");
    }
}
