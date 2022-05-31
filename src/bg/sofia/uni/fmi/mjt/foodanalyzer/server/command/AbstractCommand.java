package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.http.FoodAPIClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;

import java.net.http.HttpClient;

public abstract class AbstractCommand implements Command {

    protected FoodAPIClient apiClient;
    private final String arguments;

    public abstract String execute(FoodStorage storage) throws FoodAnalyzerException;

    protected AbstractCommand(String arguments) {
        this(arguments, new FoodAPIClient(HttpClient.newHttpClient()));
    }

    protected AbstractCommand(String arguments, FoodAPIClient apiClient) {
        this.arguments = arguments;
        this.apiClient = apiClient;
    }

    protected String getArguments() {
        return arguments;
    }

    protected void assertArgumentsNotNull() throws InvalidCommandArgumentsException {
        if (arguments == null || arguments.isBlank()) {
            throw new InvalidCommandArgumentsException("Command arguments cannot be null/empty");
        }
    }

    protected void assertStorageNotNull(FoodStorage storage) {
        if (storage == null) {
            throw new IllegalArgumentException("Food Storage cannot be null");
        }
    }
}
