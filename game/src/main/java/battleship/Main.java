package battleship;

import java.util.Scanner;

import battleship.socket.Client;
import battleship.socket.Server;

public class Main {
    static Scanner scanner;
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.out.println("Choose your role: ");
        System.out.println("0 - Server");
        System.out.println("1 - Client");
        int option = Integer.parseInt(scanner.nextLine());
        
        if (option == 0) {  
            Server server = new Server(5000);
            server.start();
        }
        else if (option == 1) {
            Client client = new Client("127.0.0.1", 5000);
            client.start();
        }
    }
}