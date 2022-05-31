package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import java.util.Objects;

public class FoodReport {

    private final String description;
    private final String ingredients;
    private final LabelNutrients labelNutrients;

    public FoodReport(String description, String ingredients, LabelNutrients labelNutrients) {
        this.description = description;
        this.ingredients = ingredients;
        this.labelNutrients = labelNutrients;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public LabelNutrients getLabelNutrients() {
        return labelNutrients;
    }

    @Override
    public String toString() {
        return "FoodReport: " +
                "description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", labelNutrients=" + labelNutrients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodReport report = (FoodReport) o;
        return Objects.equals(description, report.getDescription())
                && Objects.equals(ingredients, report.getIngredients())
                && Objects.equals(labelNutrients, report.getLabelNutrients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
