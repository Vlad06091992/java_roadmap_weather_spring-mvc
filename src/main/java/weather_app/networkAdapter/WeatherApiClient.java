package weather_app.networkAdapter;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import weather_app.exceptions.IncorrectLocationResponse;
import weather_app.exceptions.IncorrectWeatherResponse;

@Component
@AllArgsConstructor
public class WeatherApiClient {
    private final RestClient restClient;

    public String getWeatherByLocation(float lat, float lon) {
        try {
            RestClient.RequestHeadersSpec<?> uri = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder

                          .scheme("http")
                            .host("localhost")
                            .port(3000)

//                            .scheme("https")
//                            .host("api.openweathermap.org")
                            .path("data/2.5/weather")

                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", "ecf3f1f2a2b3e1d4cdc05e6752da3a60")
                            .build());


            String result = uri
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        // Handle 4xx errors (e.g., Log and throw custom exception)
                        throw new IncorrectWeatherResponse();
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        // Handle 5xx errors
                        throw new IncorrectWeatherResponse();
                    })
                    .body(String.class);

            return result;
        } catch (Exception e) {
            throw new IncorrectWeatherResponse();
        }

    }

    public String getLocationsByName(String name) {
        try {
            RestClient.RequestHeadersSpec<?> uri = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder

                            .scheme("http")
                            .host("localhost")
                            .port(3000)


//                            .scheme("https")
//                            .host("api.openweathermap.org")
                            .path("geo/1.0/direct")
                            .queryParam("q", name)
                            .queryParam("limit", 5)
                            .queryParam("appid", "ecf3f1f2a2b3e1d4cdc05e6752da3a60")
                            .build());


            String result = uri
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        // Handle 4xx errors (e.g., Log and throw custom exception)
                        throw new IncorrectLocationResponse();
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        // Handle 5xx errors
                        throw new IncorrectLocationResponse();
                    })
                    .body(String.class);

            return result;
        } catch (ResourceAccessException e) {
            System.out.println("No response from server: " + e.getMessage());
            throw new IncorrectLocationResponse();
        } catch (Exception e) {
            throw new IncorrectLocationResponse();
        }


    }

}
