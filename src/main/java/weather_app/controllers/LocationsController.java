package weather_app.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import weather_app.dto.location.LocationDTO;
import weather_app.entities.User;
import weather_app.entities.UserLocation;
import weather_app.exceptions.IncorrectLoginDataException;
import weather_app.dto.location.Location;
import weather_app.networkAdapter.WeatherData;
import weather_app.services.LocationService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LocationsController {

    private final WeatherData weatherData;
    private final LocationService locationService;

    @GetMapping("/search-location")
    public String register(@RequestParam(name = "locationName", required = false) String name, Model model, HttpServletRequest request) throws IncorrectLoginDataException {
        log.info(name);

        if(name!=null){
           List<Location> locations = locationService.getLocationsByName(name);
            log.debug("result: {}" + locations, locations);
            model.addAttribute("locations", locations);
        } else {
            log.debug("result: null");
            model.addAttribute("locations", new ArrayList<Location>());
        }
        return "search";
    }

    @PostMapping("/add-location")
    public String addLocation(@ModelAttribute LocationDTO location, @RequestAttribute("userId") String userId) throws IncorrectLoginDataException {
      log.info(location.toString());
      log.info(userId);

      UserLocation userLocation = locationService.addLocation(location,userId);

      User user = userLocation.getUser();

        return "search";
    }
}
