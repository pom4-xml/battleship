package battleship;

import java.util.Scanner;

public class Main {
    Scanner keyboard;
    BattleshipManager battleshipManager;

    public Main() {
        keyboard = new Scanner(System.in);
    }

    public void runBattleshipGame() {
         System.out.println("Running Battleship Game...");
    }

    public static void main(String[] args) {
        Main program = new Main();
        program.runBattleshipGame();
    }
}