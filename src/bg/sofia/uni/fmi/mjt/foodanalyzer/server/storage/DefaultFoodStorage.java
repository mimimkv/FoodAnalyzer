package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultFoodStorage implements FoodStorage {

    private final Map<String, List<Food>> foodMap;
    private final Map<Integer, FoodReport> foodReportMap;
    private final Map<String, Food> barCodeMap;

    public DefaultFoodStorage() {
        foodMap = new HashMap<>();
        foodReportMap = new HashMap<>();
        barCodeMap = new HashMap<>();
    }

    @Override
    public List<Food> getFoodByName(String name) {
        validateString(name);

        return foodMap.get(name);
    }

    @Override
    public FoodReport getFoodReport(int fdcId) {
        return foodReportMap.get(fdcId);
    }

    @Override
    public Food getFoodByBarCode(String barcode) {
        validateString(barcode);
        return barCodeMap.get(barcode);
    }

    @Override
    public void addFood(String foodName, List<Food> foods) {
        validateString(foodName);
        if (foods == null) {
            throw new IllegalArgumentException("Food cannot be null");
        }

        foodMap.putIfAbsent(foodName, foods);
        for (Food food : foods) {
            barCodeMap.putIfAbsent(food.getGtinUpc(), food);
        }
    }

    @Override
    public void addFoodReport(int fdcId, FoodReport foodReport) {
        if (foodReport == null) {
            throw new IllegalArgumentException("Food report cannot be null");
        }

        foodReportMap.putIfAbsent(fdcId, foodReport);
    }

    private void validateString(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("Arguments cannot be null or empty");
        }
    }

}
