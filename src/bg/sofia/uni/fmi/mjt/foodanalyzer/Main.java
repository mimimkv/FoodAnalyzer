package bg.sofia.uni.fmi.mjt.foodanalyzer;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.FoodAnalyzerServer;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.Server;

import java.util.Scanner;

public class Main {

    private static final String START_COMMAND_TEXT = "start";
    private static final String STOP_COMMAND_TEXT = "stop";

    public static void main(String[] args) {

        System.out.println("Enter start in order to start the server");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!START_COMMAND_TEXT.equalsIgnoreCase(input)) {
            System.out.println("Enter start");
            input = scanner.nextLine();
        }

        Server server = new FoodAnalyzerServer();
        Thread thread = new Thread(server);
        thread.start();
        System.out.println("Server started successfully");

        while (true) {
            input = scanner.nextLine();

            if (START_COMMAND_TEXT.equalsIgnoreCase(input)) {
                System.out.println("The server is already working");
            } else if (STOP_COMMAND_TEXT.equalsIgnoreCase(input)) {
                break;
            } else {
                System.out.println("Unknown command");
            }
        }

        server.stopServer();
        System.out.println("The server has been stopped");

    }
}
