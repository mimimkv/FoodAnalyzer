package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;

public interface Server extends Runnable {

    void startServer() throws FoodAnalyzerException;

    void stopServer();
}
