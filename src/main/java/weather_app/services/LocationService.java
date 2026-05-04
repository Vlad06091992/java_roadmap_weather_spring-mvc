package weather_app.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weather_app.dao.LocationsDao;
import weather_app.dto.location.LocationDTO;
import weather_app.entities.UserLocation;
import weather_app.dto.location.Location;
import weather_app.networkAdapter.WeatherApiClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class LocationService {
    private final WeatherApiClient weatherData;
    private final ObjectMapper objectMapper;
    private final LocationsDao locationsDao;


    public List<Location> getLocationsByName(String name) {
        try {
            String locationsResponse = weatherData.getLocationsByName(name);
            ArrayList<Location> locations = objectMapper
                    .readValue(locationsResponse, new TypeReference<ArrayList<Location>>() {
                    });
            log.info("getLocationsByName returns {} locations", locations.size());
            return locations;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public void addLocation(LocationDTO locationDTO, String userId) {
        locationsDao.addLocation(locationDTO, userId);
    }
}
