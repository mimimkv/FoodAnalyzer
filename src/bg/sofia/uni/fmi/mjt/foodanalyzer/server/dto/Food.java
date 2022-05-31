package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;


import java.util.Objects;

public class Food {

    private final int fdcId;
    private final String description;
    private final String dataType;
    private final String gtinUpc;

    public Food(int fdcId, String description, String dataType, String gtinUpc) {
        this.fdcId = fdcId;
        this.description = description;
        this.dataType = dataType;
        this.gtinUpc = gtinUpc;
    }

    public int getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getDataType() {
        return dataType;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    @Override
    public String toString() {
        return "Food: " +
                "fdcId=" + fdcId +
                ", description='" + description + '\'' +
                ", dataType='" + dataType + '\'' +
                ", gtinUpc='" + gtinUpc + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return fdcId == food.getFdcId()
                && Objects.equals(description, food.getDescription())
                && Objects.equals(dataType, food.getDataType())
                && Objects.equals(gtinUpc, food.getGtinUpc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fdcId);
    }
}
