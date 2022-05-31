package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.LabelNutrients;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Nutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.http.FoodAPIClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.DefaultFoodStorage;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFoodReportCommandTest {

    private GetFoodReportCommand command;

    @Mock
    private FoodStorage storage = new DefaultFoodStorage();

    @Mock
    private FoodAPIClient client;

    @Test
    public void testExecuteNullArguments() {
        command = new GetFoodReportCommand(null);
        String message = "Method execute should throw InvalidCommandArgumentsException when called with null arguments";
        assertThrows(InvalidCommandArgumentsException.class, () -> command.execute(storage), message);
    }

    @Test
    public void testExecuteNullStorageArgument() {
        command = new GetFoodReportCommand("dummy arguments");
        String message = "Method execute should throw IllegalArgumentException when storage object is null";
        assertThrows(IllegalArgumentException.class, () -> command.execute(null), message);
    }

    @Test
    public void testExecuteFoodIsRetrievedDirectlyFromStorage() throws FoodAnalyzerException {
        command = new GetFoodReportCommand("1");
        String message = "Method does not return the correct food report when it is retrieved directly from storage";
        FoodReport expected = new FoodReport("some food", "ingredients", new LabelNutrients(new Nutrient(1.2),
                new Nutrient(1.1), new Nutrient(1.1), new Nutrient(1.1), new Nutrient(1.2)));

        when(storage.getFoodReport(1)).thenReturn(expected);
        String actual = command.execute(storage);

        // verifies that food report is retrieved directly from storage, not from api
        verify(storage, times(0)).addFoodReport(1, expected);

        assertEquals(expected.toString(), actual, message);
    }

    @Test
    public void testExecuteFoodIsRetrievedFromAPI() throws FoodAnalyzerException {
        int fcdId = 123;
        command = new GetFoodReportCommand(String.valueOf(fcdId), client);
        String message = "Method does not return the correct food report when it is retrieved from food api";
        FoodReport expected = new FoodReport("some food", "ingredients", new LabelNutrients(new Nutrient(1.2),
                new Nutrient(1.1), new Nutrient(1.1), new Nutrient(1.1), new Nutrient(1.2)));

        when(storage.getFoodReport(fcdId)).thenReturn(null);
        when(client.requestFoodReportByFdcId(fcdId)).thenReturn(expected);
        String actual = command.execute(storage);

        assertEquals(expected.toString(), actual, message);
    }

}