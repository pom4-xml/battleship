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
        String simulatedInput = "0\n0\ntrue\n1\n0\ntrue\n2\n0\ntrue\n3\n0\ntrue\n4\n0\ntrue\n" + // Player 1
                "0\n1\ntrue\n1\n1\ntrue\n2\n1\ntrue\n3\n1\ntrue\n4\n1\ntrue\n" + // Player 2
                "0\n0\n0\n1\n1\n1\n2\n2\n"; // Shots

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

    @Test

    void testInvalidInputs() {
        String input = "a\n0\nfalse\nb\n1\ntrue\n"; // inputs inválidos
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        SimulationGameController controller = new SimulationGameController();
        assertDoesNotThrow(controller::startGame);

        // Restaurar System.in
        System.setIn(originalIn);
    }

    @Test
    void testShotOutOfBoundsHandled() {
        // Coloca barcos para los jugadores
        String input = "0\n0\ntrue\n1\n0\ntrue\n2\n0\ntrue\n3\n0\ntrue\n4\n0\ntrue\n" + // Player 1
                "0\n1\ntrue\n1\n1\ntrue\n2\n1\ntrue\n3\n1\ntrue\n4\n1\ntrue\n" + // Player 2
                "10\n-1\n0\n0\n0\n1\n"; // Tiros: primero inválidos, luego válidos

        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        SimulationGameController controller = new SimulationGameController();
        assertDoesNotThrow(controller::startGame);

        System.setIn(originalIn);
    }

    @Test
    void testGameLoopPlayerWins() {
        String simulatedInput = "0\n0\ntrue\n" +
                "0\n0\ntrue\n" +
                "0\n0\n0\n1\n" +
                "9\n9\n9\n8\n";

        ByteArrayInputStream bais = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(bais);

        SimulationGameController controller = new SimulationGameController();

        // Solo comprobar que no lanza excepción
        assertDoesNotThrow(controller::startGame);

        System.setIn(originalIn);
    }


}
