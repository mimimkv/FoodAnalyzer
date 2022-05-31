package bg.sofia.uni.fmi.mjt.foodanalyzer.server.http;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodCollection;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class FoodAPIClient {

    private static Gson gson;

    private static final String API_KEY = "FDsMlWxdNU4NQc1suAjUMiT4ZYzAkDyQglrDhLf5";
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "api.nal.usda.gov";
    private static final String API_ENDPOINT_GET_FOOD_PATH = "/fdc/v1/foods/search";
    private static final String API_ENDPOINT_GET_REPORT_PATH = "/fdc/v1/food/";
    private static final String API_ENDPOINT_GET_FOOD_QUERY = "query=%s&requireAllWords=true&api_key=%s";
    private static final String API_ENDPOINT_GET_REPORT_QUERY = "api_key=%s";

    private final String apiKey;
    private final HttpClient client;

    public FoodAPIClient(HttpClient client) {
        this(client, API_KEY);
    }

    public FoodAPIClient(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;

        gson = new GsonBuilder().create();
    }

    public FoodCollection requestFoodByName(String name) throws FoodAnalyzerException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name argument cannot be null/empty");
        }

        HttpResponse<String> response;

        try {
            URI uri = new URI(API_ENDPOINT_SCHEME,
                    API_ENDPOINT_HOST,
                    API_ENDPOINT_GET_FOOD_PATH,
                    String.format(API_ENDPOINT_GET_FOOD_QUERY, name, apiKey),
                    null);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri).build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new FoodAnalyzerException("Problem occurred while requesting food by name", e);
        }

        validateStatusCode(response);

        return gson.fromJson(response.body(), FoodCollection.class);
    }

    public FoodReport requestFoodReportByFdcId(int fdcId) throws FoodAnalyzerException {

        HttpResponse<String> response;

        try {
            URI uri = new URI(
                    API_ENDPOINT_SCHEME,
                    API_ENDPOINT_HOST,
                    API_ENDPOINT_GET_REPORT_PATH + fdcId,
                    String.format(API_ENDPOINT_GET_REPORT_QUERY, apiKey),
                    null);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new FoodAnalyzerException("Problem occurred while requesting food report", e);
        }

        validateStatusCode(response);

        return gson.fromJson(response.body(), FoodReport.class);
    }

    private void validateStatusCode(HttpResponse<String> response) throws FoodAnalyzerException {
        if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new FoodNotFoundException("No food matched your request");
        } else if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw new FoodAnalyzerException("Problem occurred. Try again later.");
        }
    }

}
