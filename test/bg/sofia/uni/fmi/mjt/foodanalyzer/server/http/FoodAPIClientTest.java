package bg.sofia.uni.fmi.mjt.foodanalyzer.server.http;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodCollection;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.LabelNutrients;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Nutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodAnalyzerException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodAPIClientTest {

    private FoodAPIClient client;

    private static Gson gson;

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    @BeforeAll
    public static void setUpGson() {
        gson = new GsonBuilder().create();
    }

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        Mockito.lenient().when(httpClientMock.send(Mockito.any(HttpRequest.class),
                        Mockito.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        client = new FoodAPIClient(httpClientMock);
    }

    @Test
    public void testRequestFoodByNameNullArgument() {
        String message = "IllegalArgumentException expected when method requestFoodByName is called with null argument";
        assertThrows(IllegalArgumentException.class, () -> client.requestFoodByName(null), message);
    }

    @Test
    public void testRequestFoodByNameOneFoodIsReturned() throws Exception {
        String message =
                "Method requestFoodByName does not return collection with one food when there is one food with the same description in food api";

        FoodCollection expected = new FoodCollection(List.of(new Food(1, "some food", "branded", "123456")));
        String json = gson.toJson(expected, FoodCollection.class);

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(json);

        FoodCollection actual = client.requestFoodByName("some food");
        assertEquals(expected, actual, message);
    }

    @Test
    public void testRequestFoodByNameEmptyCollectionReturned() throws Exception {
        String message =
                "Method requestFoodByName does not return an empty collection when there is no food found";

        FoodCollection expected = new FoodCollection(Collections.emptyList());
        String json = gson.toJson(expected, FoodCollection.class);

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(json);

        FoodCollection actual = client.requestFoodByName("some food");
        assertEquals(expected, actual, message);
    }

    @Test
    public void testRequestFoodReportByFdcIdSuccess() throws Exception {
        String message = "Method does not return collection with one food when there is one food with the same fdcId in food api";
        FoodReport expected = new FoodReport("food", "foods", new LabelNutrients(new Nutrient(1.2),
                new Nutrient(1.2), new Nutrient(2.2), new Nutrient(1.1), new Nutrient(1.1)));

        String json = gson.toJson(expected, FoodReport.class);

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(json);

        FoodReport actual = client.requestFoodReportByFdcId(123);
        assertEquals(expected, actual, message);
    }

    @Test
    public void testRequestFoodReportHttpConnectionNotFound() {
        String message = "FoodNotFoundException expected when response from food api has status code: HTTP_NOT_FOUND";

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
        assertThrows(FoodNotFoundException.class, () -> client.requestFoodReportByFdcId(123), message);
    }

    @Test
    public void testRequestFoodReportHttpStatusCodeNotOK() {
        String message = "FoodAnalyzerException expected when response from food api has status code different from HTTP_OK";

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        assertThrows(FoodAnalyzerException.class, () -> client.requestFoodReportByFdcId(123), message);
    }

}