package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;

import java.util.List;

public interface FoodStorage {

    List<Food> getFoodByName(String name);

    FoodReport getFoodReport(int fdcId);

    Food getFoodByBarCode(String barcode);

    void addFood(String name, List<Food> foods);

    void addFoodReport(int fdcId, FoodReport foodReport);

}