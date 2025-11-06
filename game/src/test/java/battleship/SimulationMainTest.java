package battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

class SimulationMainTest {

    @Test
    void testMainRunsWithoutException() {
        String fakeInput = "0\n0\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(fakeInput.getBytes()));

        assertDoesNotThrow(() -> SimulationMain.main(new String[]{}),
                "SimulationMain.main() should not throw any exception");

        System.setIn(originalIn);
    }
}
