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
                "0 1 true\n" +
                "5 5 false\n" +
                "5 6 false\n" +
                "0 0\n" +
                "5 5\n";

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