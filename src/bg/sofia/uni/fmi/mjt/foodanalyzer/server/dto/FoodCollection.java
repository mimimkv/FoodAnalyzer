package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FoodCollection {
    private final List<Food> foods;

    public FoodCollection(List<Food> foods) {
        this.foods = foods;
    }

    public List<Food> getFoods() {
        return foods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodCollection that = (FoodCollection) o;
        return Objects.equals(foods, that.foods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foods);
    }

    @Override
    public String toString() {
        return foods.stream()
                .map(Food::toString)
                .collect(Collectors.joining(", "));
    }
}
