package battleship;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimulationGameControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "0 0 true\n0 1 true\n5 5 false\n5 6 false\n0 0\n5 5\n",
            "1 0 true\n1 1 false\n6 6 true\n6 7 false\n1 0\n6 6\n"
    })
    void testControllerInitializes(String inputSequence) {
        InputStream originalIn = System.in;
        ByteArrayInputStream bais = new ByteArrayInputStream(inputSequence.getBytes());
        System.setIn(bais);

        try {
            SimulationGameController controller = new SimulationGameController();
            controller.startGame();
            assertNotNull(controller, "Controller should initialize correctly");
        } finally {
            System.setIn(originalIn);
        }
    }
}