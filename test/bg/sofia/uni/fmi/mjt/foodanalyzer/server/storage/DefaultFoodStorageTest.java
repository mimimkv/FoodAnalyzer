package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.LabelNutrients;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Nutrient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultFoodStorageTest {

    private final FoodStorage storage = new DefaultFoodStorage();

    @Test
    public void testGetFoodByNameNullArgument() {
        String message = "Method getFoodByName should throw IllegalArgumentException when called with null argument";
        assertThrows(IllegalArgumentException.class, () -> storage.getFoodByName(null), message);
    }

    @Test
    public void testGetFoodByNameNoFoodInStorage() {
        String message = "Method getFoodByName should return null when the storage is empty";
        List<Food> actual = storage.getFoodByName("some food");

        assertIterableEquals(null, actual, message);
    }

    @Test
    public void testGetFoodByNameOneFoodReturned() {
        String foodName = "food";
        String message = "Method getFoodByName does not return the correct food when there is only one food matching the name in the storage";

        List<Food> expected = List.of(new Food(1, "some food", "none", "123"));
        storage.addFood(foodName, expected);
        List<Food> actual = storage.getFoodByName(foodName);

        assertIterableEquals(expected, actual, message);
    }

    @Test
    public void testGetFoodByNameOneFoodTwiceAdded() {
        String foodName = "food";
        String message = "Method getFoodByName does not return the correct food when the same food is added twice in the storage";

        List<Food> expected = List.of(new Food(1, "some food", "none", "123"));
        storage.addFood(foodName, expected);
        storage.addFood(foodName, expected);
        List<Food> actual = storage.getFoodByName(foodName);

        assertIterableEquals(expected, actual, message);
    }

    @Test
    public void testAddFoodNullArgument() {
        String message = "IllegalArgumentException expected when trying to add null collection in the storage";
        assertThrows(IllegalArgumentException.class, () -> storage.addFood("null", null), message);
    }

    @Test
    public void testAddFoodReportNullArgument() {
        String message = "IllegalArgumentException expected when trying to add null food report in the storage";
        assertThrows(IllegalArgumentException.class, () -> storage.addFoodReport(1, null), message);
    }

    @Test
    public void testAddFoodReportNoReportsInStorage() {
        String message = "Null should be returned when there are no registered reports in the storage";
        assertNull(storage.getFoodReport(1), message);
    }

    @Test
    public void testGetFoodReportOneReportInStorage() {
        String message = "Method getFoodReport does not return the correct report when there is a report in the storage for the given food";
        int fdcId = 1;
        FoodReport expected = new FoodReport("food", "bread",
                new LabelNutrients(new Nutrient(1.0), new Nutrient(1.0), new Nutrient(1.0),
                        new Nutrient(1.0), new Nutrient(1.0)));

        storage.addFoodReport(fdcId, expected);
        FoodReport actual = storage.getFoodReport(fdcId);

        assertEquals(expected, actual, message);
    }

    @Test
    public void testGetFoodByBarcodeWithOneFoodInStorage() {
        String message = "Method getFoodByBarcode does not return the correct food when there is food with the given barcode in the storage";
        String gtinUpc = "123456";
        Food expected = new Food(1, "food1", "branded", gtinUpc);

        storage.addFood(expected.getDescription(), List.of(expected));
        assertEquals(expected, storage.getFoodByBarCode(gtinUpc), message);
    }

}