package battleship;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationMainTest {

    @Test
    void testMainRunsWithoutException() {
        String simulatedInput =
                "0 0 true\n" +
                "1 0 true\n" +
                "0 0 true\n" +
                "1 0 true\n" +
                "0 0\n" +
                "0 0\n" +
                "1 0\n" +
                "1 0\n";

        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        try {
            assertDoesNotThrow(() -> SimulationMain.main(new String[]{}),
                    "The main method should run without throwing exceptions");
        } finally {
            System.setIn(originalIn);
        }
    }
}