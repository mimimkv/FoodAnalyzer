package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.http.FoodAPIClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorage;


public class GetFoodReportCommand extends AbstractCommand {

    private static final String INVALID_FDC_ID_MESSAGE = "Invalid fdcId: it should contain only digits";

    public GetFoodReportCommand(String arguments) {
        super(arguments);
    }

    public GetFoodReportCommand(String arguments, FoodAPIClient apiClient) {
        super(arguments, apiClient);
    }

    @Override
    public String execute(FoodStorage storage) throws FoodAnalyzerException {
        assertArgumentsNotNull();
        assertStorageNotNull(storage);

        int fdcId;
        try {
            fdcId = Integer.parseInt(getArguments());
        } catch (NumberFormatException e) {
            return INVALID_FDC_ID_MESSAGE;
        }
        FoodReport foodReport = storage.getFoodReport(fdcId);

        if (foodReport == null) {
            foodReport = apiClient.requestFoodReportByFdcId(fdcId);
            storage.addFoodReport(fdcId, foodReport);
        }

        return foodReport.toString();
    }
}
