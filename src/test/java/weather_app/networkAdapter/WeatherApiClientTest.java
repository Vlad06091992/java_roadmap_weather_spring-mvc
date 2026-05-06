package weather_app.networkAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import weather_app.exceptions.IncorrectLocationResponse;
import weather_app.exceptions.IncorrectWeatherResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.hamcrest.Matchers.containsString;

class WeatherApiClientTest {

    private MockRestServiceServer mockServer;
    private WeatherApiClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        client = new WeatherApiClient(builder.build());
    }

    // --- getWeatherByLocation ---

    @Test
    void getWeatherByLocation_success() {
        String body = "{\"weather\":[{\"description\":\"clear sky\"}]}";
        mockServer.expect(requestTo(containsString("/data/2.5/weather")))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        String result = client.getWeatherByLocation(51.5f, -0.1f);

        assertEquals(body, result);
        mockServer.verify();
    }

    @Test
    void getWeatherByLocation_4xx_throwsIncorrectWeatherResponse() {
        mockServer.expect(requestTo(containsString("/data/2.5/weather")))
                .andRespond(withBadRequest());

        assertThrows(IncorrectWeatherResponse.class,
                () -> client.getWeatherByLocation(51.5f, -0.1f));
    }

    @Test
    void getWeatherByLocation_5xx_throwsIncorrectWeatherResponse() {
        mockServer.expect(requestTo(containsString("/data/2.5/weather")))
                .andRespond(withServerError());

        assertThrows(IncorrectWeatherResponse.class,
                () -> client.getWeatherByLocation(51.5f, -0.1f));
    }

    // --- getLocationsByName ---

    @Test
    void getLocationsByName_success() {
        String body = "[{\"name\":\"London\",\"lat\":51.5,\"lon\":-0.1}]";
        mockServer.expect(requestTo(containsString("/geo/1.0/direct")))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        String result = client.getLocationsByName("London");

        assertEquals(body, result);
        mockServer.verify();
    }

    @Test
    void getLocationsByName_4xx_throwsIncorrectLocationResponse() {
        mockServer.expect(requestTo(containsString("/geo/1.0/direct")))
                .andRespond(withBadRequest());

        assertThrows(IncorrectLocationResponse.class,
                () -> client.getLocationsByName("London"));
    }

    @Test
    void getLocationsByName_5xx_throwsIncorrectLocationResponse() {
        mockServer.expect(requestTo(containsString("/geo/1.0/direct")))
                .andRespond(withServerError());

        assertThrows(IncorrectLocationResponse.class,
                () -> client.getLocationsByName("London"));
    }
}
