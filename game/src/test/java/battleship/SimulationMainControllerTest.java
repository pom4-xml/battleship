package battleship;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimulationGameControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "0 0 true\n1 0 true\n0 0 true\n1 0 true\n0 0\n0 0\n",
            "0 1 true\n2 2 true\n0 1 true\n2 2 true\n1 1\n1 0\n"
    })
    void testControllerInitializes(String inputSequence) {
        InputStream originalIn = System.in;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(inputSequence.getBytes())) {
            System.setIn(bais);
            SimulationGameController controller = new SimulationGameController();
            controller.startGame();
            assertNotNull(controller);
        } catch (IOException e) {
            // ByteArrayInputStream nunca lanza IOException realmente
        } finally {
            System.setIn(originalIn);
        }
    }
}
