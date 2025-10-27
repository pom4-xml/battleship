package battleship;

import java.util.Scanner;

public class Main {
    Scanner keyboard;
    BattleshipManager battleshipManager;

    public Main() {
        keyboard = new Scanner(System.in);
    }

    /*
    private void initializeShips(Player player) {
       battleshipManager.initializeShips(player);
    }
    */


    public static void main(String[] args) {
        Main program = new Main();
        //program.runBattleshipGame();
    }
}