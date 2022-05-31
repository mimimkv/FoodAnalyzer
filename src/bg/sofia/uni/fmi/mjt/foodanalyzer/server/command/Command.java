package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;


public interface Command {

    String execute(FoodStorage foodStorage) throws FoodAnalyzerException;
}
