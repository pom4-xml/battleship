package battleship;

import java.util.List;
import java.util.Scanner;

public class SimulationGameController {
    private Player player1;
    private Player player2;

    public SimulationGameController() {
        player1 = new Player("Jugador 1", BattleshipManager.createStandardShips());
        player2 = new Player("Jugador 2", BattleshipManager.createStandardShips());
    }

    public void runGame() {
        Scanner sc = new Scanner(System.in);

        // Colocación de barcos
        player1.placeShipsManually(sc);
        player2.placeShipsManually(sc);

        boolean player1Turn = true;

        while (true) {
            Player current = player1Turn ? player1 : player2;
            Player enemy = player1Turn ? player2 : player1;

            System.out.println("\nTurno de " + current.getName());
            System.out.print("Introduce coordenadas (fila columna): ");
            int x = sc.nextInt();
            int y = sc.nextInt();

            Position shot = new Position(x, y);
            String result = current.shootAt(shot, enemy);

            System.out.println("Resultado del disparo: " + result);

            if (enemy.hasLost()) {
                System.out.println(current.getName() + " gana la partida!");
                break;
            }

            player1Turn = !player1Turn;
        }

        sc.close();
    }

    public static void main(String[] args) {
        SimulationGameController game = new SimulationGameController();
        game.runGame();
    }
}
