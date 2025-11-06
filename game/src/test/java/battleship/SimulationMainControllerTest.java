package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationGameControllerTest {

    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        originalIn = System.in;
    }

    @Test
    void testControllerInitializes() {
        String simulatedInput =
                "0\n0\ntrue\n1\n0\ntrue\n2\n0\ntrue\n3\n0\ntrue\n4\n0\ntrue\n" +  // Player 1
                "0\n1\ntrue\n1\n1\ntrue\n2\n1\ntrue\n3\n1\ntrue\n4\n1\ntrue\n" +  // Player 2
                "0\n0\n0\n1\n1\n1\n2\n2\n";                                       // Shots

        try (ByteArrayInputStream bais = new ByteArrayInputStream(simulatedInput.getBytes())) {
            System.setIn(bais);

            assertDoesNotThrow(() -> {
                SimulationGameController controller = new SimulationGameController();
                controller.startGame();
            }, "Game controller should initialize and run without throwing exceptions");
        } catch (Exception ignored) {
            // Ignored
        } finally {
            System.setIn(originalIn);
        }
    }
}
