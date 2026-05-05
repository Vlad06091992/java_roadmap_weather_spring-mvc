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

    public <T> T extractSafetyValue(JsonNode jsonNode, Class<T> targetType) {

        if (jsonNode != null && !jsonNode.isNull()) {
            if (targetType == String.class) {
                return targetType.cast(jsonNode.asText());
            }

            if (targetType == Byte.class) {
                return targetType.cast(Byte.valueOf((byte) jsonNode.asInt()));
            }

            if (targetType == Float.class) {
                return targetType.cast(Float.valueOf((float) jsonNode.asDouble()));
            }

        } else {
            if (targetType == String.class) {
                return null;
            }

            if (targetType == Byte.class) {
                return targetType.cast(Byte.valueOf((byte) 0));
            }

            if (targetType == Float.class) {
                return targetType.cast(Float.valueOf((float) 0.0));
            }
        }
        return null;
    }

    private WeatherResponseDTO mapResponse(String json) {

        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            log.debug("START");
            String name = extractSafetyValue(jsonNode.get("name"), String.class);
            String country = extractSafetyValue(jsonNode.get("sys").get("country"), String.class);
            String main = extractSafetyValue(jsonNode.get("weather").get(0).get("main"), String.class);
            String description = extractSafetyValue(jsonNode.get("weather").get(0).get("description"), String.class);
            String icon = extractSafetyValue(jsonNode.get("weather").get(0).get("icon"), String.class);
            byte humidity = extractSafetyValue(jsonNode.get("main").get("humidity"), Byte.class);
            float kelvinTemperature = extractSafetyValue(jsonNode.get("main").get("temp"), Float.class);
            float kelvinFeelsLikeTemperature = extractSafetyValue(jsonNode.get("main").get("feels_like"), Float.class);
            float lon = extractSafetyValue(jsonNode.get("coord").get("lon"), Float.class);
            float lat = extractSafetyValue(jsonNode.get("coord").get("lat"), Float.class);

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
        } catch (RuntimeException ex) {
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
