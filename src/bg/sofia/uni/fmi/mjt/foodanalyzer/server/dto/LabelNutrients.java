package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import java.util.Objects;

public class LabelNutrients {

    private final Nutrient protein;
    private final Nutrient fat;
    private final Nutrient carbohydrates;
    private final Nutrient calories;
    private final Nutrient fiber;

    public LabelNutrients(Nutrient protein, Nutrient fat, Nutrient carbohydrates, Nutrient calories, Nutrient fiber) {
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.fiber = fiber;
    }

    public Nutrient getProtein() {
        return protein;
    }

    public Nutrient getFat() {
        return fat;
    }

    public Nutrient getCarbohydrates() {
        return carbohydrates;
    }

    public Nutrient getCalories() {
        return calories;
    }

    public Nutrient getFiber() {
        return fiber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabelNutrients that = (LabelNutrients) o;
        return Objects.equals(protein, that.getProtein())
                && Objects.equals(fat, that.getFat())
                && Objects.equals(carbohydrates, that.getCarbohydrates())
                && Objects.equals(calories, that.getCalories())
                && Objects.equals(fiber, that.getFiber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(protein, fat, carbohydrates, calories, fiber);
    }

    @Override
    public String toString() {
        return "LabelNutrients: " +
                "protein=" + protein +
                ", fat=" + fat +
                ", carbohydrates=" + carbohydrates +
                ", calories=" + calories +
                ", fiber=" + fiber;
    }
}
