package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;


public class FoodAnalyzerException extends Exception {

    public FoodAnalyzerException(String message) {
        super(message);
    }

    public FoodAnalyzerException(String message, Exception e) {
        super(message, e);
    }

}
