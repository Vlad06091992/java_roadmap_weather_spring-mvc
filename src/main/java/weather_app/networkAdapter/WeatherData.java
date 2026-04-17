package weather_app.networkAdapter;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherData {

    private final RestClient restClient;

    public WeatherData(RestClient restClient) {
    this.restClient = restClient;
    }

   public String getWeatherByLocation(){
        String result = restClient
                .get()
                .uri("https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=ecf3f1f2a2b3e1d4cdc05e6752da3a60")
                .retrieve()
                .body(String.class);

        return result;
    }


    public String getWeatherByL(){
        String result = restClient
                .get()
                .uri("https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=ecf3f1f2a2b3e1d4cdc05e6752da3a60")
                .retrieve()
                .body(String.class);

        return result;
    }
}
