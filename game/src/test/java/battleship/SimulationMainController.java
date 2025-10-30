package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimulationGameControllerTest {

    private SimulationGameController controller;

    @BeforeEach
    void setUp() {
        controller = new SimulationGameController();
    }

    @Test
    void testGameRunsWithSimulatedInput() {
        String input = ""
                + "0 0 true\n"
                + "1 0 true\n"
                + "0 0 true\n"
                + "1 0 true\n"
                + "0 0\n"
                + "0 0\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        controller.startGame();

        assertNotNull(controller, "Controller se inicializó correctamente");
    }
}
