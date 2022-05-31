package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodCollection;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.http.FoodAPIClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.DefaultFoodStorage;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFoodCommandTest {

    private static final String NO_FOODS_FOUND_MESSAGE = "No foods found with name %s";
    private static final String FOOD_NAME = "raffaello treat";

    private GetFoodCommand command;

    @Mock
    private FoodStorage storage = new DefaultFoodStorage();

    @Mock
    private FoodAPIClient client;

    @Test
    public void testExecuteNullArguments() {
        command = new GetFoodCommand(null);
        String message = "Method execute should throw InvalidCommandArgumentsException when called with null arguments";
        assertThrows(InvalidCommandArgumentsException.class, () -> command.execute(storage), message);
    }

    @Test
    public void testExecuteNullStorageArgument() {
        command = new GetFoodCommand("dummy arguments");
        String message = "Method execute should throw IllegalArgumentException when storage object is null";
        assertThrows(IllegalArgumentException.class, () -> command.execute(null), message);
    }

    @Test
    public void testExecuteOneFoodRetrievedDirectlyFromStorage() throws FoodAnalyzerException {
        command = new GetFoodCommand(FOOD_NAME);
        String message = "Method execute does not return the correct string even though there is a food with the given description in the storage";

        Food food = new Food(1, "treat", "branded", "123");
        List<Food> foods = Collections.singletonList(food);
        when(storage.getFoodByName(FOOD_NAME)).thenReturn(foods);

        String expected = food.toString();
        String actual = command.execute(storage);

        // guarantees that food is retrieved directly from storage, not from api
        verify(storage, times(0)).addFood(FOOD_NAME, foods);

        assertEquals(expected, actual, message);
    }

    @Test
    public void testExecuteNoFoodRetrievedFromFoodAPI() throws FoodAnalyzerException {
        command = new GetFoodCommand(FOOD_NAME, client);
        String message = "Method does not return no foods when there are no foods in the food api with this description";

        when(storage.getFoodByName(FOOD_NAME)).thenReturn(null);
        when(client.requestFoodByName(FOOD_NAME)).thenReturn(new FoodCollection(Collections.emptyList()));

        assertEquals(String.format(NO_FOODS_FOUND_MESSAGE, FOOD_NAME), command.execute(storage), message);
    }

    @Test
    public void testExecuteOneFoodRetrievedFromFoodAPI() throws FoodAnalyzerException {
        command = new GetFoodCommand(FOOD_NAME, client);
        String message = "Method does not return the correct food when there is one food matching the description in the food api";

        Food food = new Food(1, "treat", "branded", "123");
        List<Food> foods = Collections.singletonList(food);

        when(storage.getFoodByName(FOOD_NAME)).thenReturn(null);
        when(client.requestFoodByName(FOOD_NAME)).thenReturn(new FoodCollection(foods));
        String expected = food.toString();
        String actual = command.execute(storage);

        assertEquals(expected, actual, message);
    }

}