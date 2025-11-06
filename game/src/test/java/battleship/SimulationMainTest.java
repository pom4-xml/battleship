package battleship;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationMainTest {

    @Test
    void testMainRunsWithoutException() {
        String simulatedInput =
                "0\n0\ntrue\n1\n0\ntrue\n0\n0\ntrue\n1\n0\ntrue\n0\n0\n0\n0\n";
        InputStream originalIn = System.in;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(simulatedInput.getBytes())) {
            System.setIn(bais);
            assertDoesNotThrow(() ->
                    SimulationMain.main(new String[]{}),
                    "The main method should run without throwing exceptions");
        } catch (Exception ignored) {
            // Ignored exception
        } finally {
            System.setIn(originalIn);
        }
    }
}