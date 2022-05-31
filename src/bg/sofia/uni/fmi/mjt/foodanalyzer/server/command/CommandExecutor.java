package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.DefaultFoodStorage;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;

public class CommandExecutor {

    private final FoodStorage foodStorage;

    public CommandExecutor() {
        foodStorage = new DefaultFoodStorage();
    }

    public String executeCommand(String input) throws FoodAnalyzerException {
        Command command = CommandFactory.getCommand(input);
        return command.execute(foodStorage);
    }


}
