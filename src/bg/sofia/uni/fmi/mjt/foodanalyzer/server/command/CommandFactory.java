package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandArgumentsException;

public class CommandFactory {

    private static final int COMMAND_INDEX = 0;
    private static final int INVALID_TOKENS_COUNT = 1;
    private static final String GET_FOOD_COMMAND_TEXT = "get-food";
    private static final String GET_FOOD_REPORT_COMMAND_TEXT = "get-food-report";
    private static final String GET_FOOD_BY_BARCODE_COMMAND_TEXT = "get-food-by-barcode";

    public static Command getCommand(String input) throws InvalidCommandException, InvalidCommandArgumentsException {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null/empty");
        }

        input = input.replaceAll(System.lineSeparator(), "");
        String[] tokens = input.split(" ", 2);

        if (tokens.length == INVALID_TOKENS_COUNT) {
            throw new InvalidCommandArgumentsException("Command cannot be executed without arguments");
        }

        return switch (tokens[COMMAND_INDEX]) {
            case GET_FOOD_COMMAND_TEXT -> new GetFoodCommand(tokens[1]);
            case GET_FOOD_REPORT_COMMAND_TEXT -> new GetFoodReportCommand(tokens[1]);
            case GET_FOOD_BY_BARCODE_COMMAND_TEXT -> new GetFoodByBarcode(tokens[1]);
            default -> throw new InvalidCommandException(String.format("%s is invalid command", tokens[0]));
        };
    }

}
