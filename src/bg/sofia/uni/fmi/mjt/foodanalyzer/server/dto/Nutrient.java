package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import java.util.Objects;

public class Nutrient {

    private final double value;

    public Nutrient(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nutrient nutrient = (Nutrient) o;
        return Double.compare(nutrient.getValue(), value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
