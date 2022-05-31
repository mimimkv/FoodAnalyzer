package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFoodByBarcodeTest {

    private static final String BARCODE = "009800146130";
    private static final String NO_FOOD_FOUND_MESSAGE = "No food matched your request";

    private Command command;

    @Mock
    private FoodStorage storage;

    @Test
    public void testExecuteWithCodeArgumentSuccess() throws FoodAnalyzerException {
        String message = "Method execute does not return the correct food when food is searched only by barcode";
        String arguments = "--code=" + BARCODE;
        command = new GetFoodByBarcode(arguments);

        Food food = new Food(1, "food", "branded", BARCODE);
        when(storage.getFoodByBarCode(BARCODE)).thenReturn(food);

        assertEquals(food.toString(), command.execute(storage), message);
    }

    @Test
    public void testExecuteWithCodeArgumentNoFoodMatchedTheRequest() throws FoodAnalyzerException {
        String message =
                "Method should return NO_FOOD_FOUND_MESSAGE when food is searched only by barcode and there is no food in the storage that matches this request";
        String arguments = "--code=" + BARCODE;
        command = new GetFoodByBarcode(arguments);

        when(storage.getFoodByBarCode(BARCODE)).thenReturn(null);

        assertEquals(NO_FOOD_FOUND_MESSAGE, command.execute(storage), message);
    }

    @Test
    public void testExecuteWithTwoArgumentsSuccess() throws FoodAnalyzerException {
        // this test demonstrates that if both arguments are present, image is ignored
        String message = "Method does not return the correct food message when food that is contained in the storage is searched by two parameters";
        String arguments = "--image=somepathtoimage " + "--code=" + BARCODE;
        command = new GetFoodByBarcode(arguments);

        Food food = new Food(1, "food", "branded", BARCODE);
        when(storage.getFoodByBarCode(BARCODE)).thenReturn(food);

        assertEquals(food.toString(), command.execute(storage), message);
    }

    @Test
    public void testExecuteWithInvalidArguments() {
        String message = "InvalidCommandArgumentsException expected when method is called with invalid arguments: \"--code\" and \"--image\" are valid";
        String arguments = "--file=file1";
        command = new GetFoodByBarcode(arguments);

        assertThrows(InvalidCommandArgumentsException.class, () -> command.execute(storage), message);
    }

    @Test
    public void testExecuteWithImageArgument() throws FoodAnalyzerException {
        String message = "Method does not return the correct food message when food is searched only by image parameter";
        String[] tokens = new String[]{"resources", "images", "barcode.gif"};
        String arguments = "--image=" + String.join(File.separator, tokens);

        command = new GetFoodByBarcode(arguments);
        Food food = new Food(1, "food", "branded", BARCODE);
        when(storage.getFoodByBarCode(BARCODE)).thenReturn(food);

        assertEquals(food.toString(), command.execute(storage), message);
    }

}