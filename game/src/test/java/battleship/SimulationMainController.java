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
        // no crear el controller aquí si depende de System.in
    }
/* 
    @Test
    void testGameRunsWithSimulatedInput() {
        String input = ""
                + "0 0 true\n"
                + "1 0 true\n"
                + "0 0 true\n"
                + "1 0 true\n"
                + "0 0\n"
                + "0 0\n";

        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            controller = new SimulationGameController(); // crear después de setIn
            controller.startGame();
            assertNotNull(controller, "Controller se inicializó correctamente");
        } finally {
            System.setIn(originalIn);
        }
    }
        */
}
