package battleship;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationMainTest {

    @Test
    void testMainRunsWithoutException() {
        String simulatedInput = "0 0 true\n1 0 true\n0 0 true\n1 0 true\n0 0\n0 0\n";
        InputStream originalIn = System.in;

        ByteArrayInputStream bais = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(bais);

        try {
            assertDoesNotThrow(() -> SimulationMain.main(new String[]{}),
                    "The main method should run without throwing exceptions");
        } finally {
            System.setIn(originalIn);
        }
    }
}