package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodCollection;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.http.FoodAPIClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;

import java.util.List;

public class GetFoodCommand extends AbstractCommand {

    private static final int EMPTY_COLLECTION_SIZE = 0;
    private static final String NO_FOODS_FOUND_MESSAGE = "No foods found with name %s";

    public GetFoodCommand(String arguments) {
        super(arguments);
    }

    public GetFoodCommand(String arguments, FoodAPIClient apiClient) {
        super(arguments, apiClient);
    }

    @Override
    public String execute(FoodStorage storage) throws FoodAnalyzerException {
        assertArgumentsNotNull();
        assertStorageNotNull(storage);

        List<Food> foods = storage.getFoodByName(getArguments());
        if (foods == null) {
            foods = apiClient.requestFoodByName(getArguments()).getFoods();

            if (foods.size() == EMPTY_COLLECTION_SIZE) {
                return String.format(NO_FOODS_FOUND_MESSAGE, getArguments());
            }

            storage.addFood(getArguments(), foods);
        }

        return new FoodCollection(foods).toString();
    }

}
