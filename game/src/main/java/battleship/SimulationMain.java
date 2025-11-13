package battleship;


public class SimulationMain {
    private SimulationGameController controller;

    public SimulationMain() {
        // No instanciamos el controlador aquí
    }

    public void run() {
        if (controller == null) {
            controller = new SimulationGameController();
        }
        controller.startGame();
    }

    public static void main(String[] args) {
        new SimulationMain().run();
    }
}
