package weather_app.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weather_app.dao.LocationsDao;
import weather_app.dto.weather.WeatherResponseDTO;
import weather_app.entities.UserLocation;
import weather_app.networkAdapter.WeatherApiClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class WeatherService {

    private final ObjectMapper objectMapper;
    private final WeatherApiClient weatherData;
    private final LocationsDao locationsDao;

    private WeatherResponseDTO mapResponse(String json) {

        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            String name = jsonNode.get("name").asText();
            String country = jsonNode.get("sys").get("country").asText();
            String main = jsonNode.get("weather").get(0).get("main").asText();
            String description = jsonNode.get("weather").get(0).get("description").asText();
            String icon = jsonNode.get("weather").get(0).get("icon").asText();
            byte humidity = Byte.parseByte(jsonNode.get("main").get("humidity").asText());
            float kelvinTemperature = Float.parseFloat(jsonNode.get("main").get("temp").asText());
            float kelvinFeelsLikeTemperature = Float.parseFloat(jsonNode.get("main").get("feels_like").asText());
            float lon = Float.parseFloat(jsonNode.get("coord").get("lon").asText());
            float lat = Float.parseFloat(jsonNode.get("coord").get("lat").asText());

            WeatherResponseDTO weatherDTO = WeatherResponseDTO.builder()
                    .country(country)
                    .name(name)
                    .main(main)
                    .humidity(humidity)
                    .description(description)
                    .icon(icon)
                    .kelvinFeelsLikeTemperature(kelvinFeelsLikeTemperature)
                    .kelvinTemperature(kelvinTemperature)
                    .lon(lon)
                    .lat(lat)
                    .build();

            log.info("json {}", name);

            return weatherDTO;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Fatal JSON error", ex);
        }

    }

    public WeatherResponseDTO getWeatherResponseDTO(float lat, float lon) {
        String json = weatherData.getWeatherByLocation(lat, lon);
        return mapResponse(json);
    }

    public List<WeatherResponseDTO> getWeatherResponsesDTO(String userId) {

        List<UserLocation> userLocations = locationsDao.getLocations(userId);

        List<WeatherResponseDTO> weatherResponsesDTO = userLocations
                .stream()
                .map((ul) -> getWeatherResponseDTO(ul.getLatitude(), ul.getLongitude()))
                .collect(Collectors.toList());

        return weatherResponsesDTO;
    }
}
