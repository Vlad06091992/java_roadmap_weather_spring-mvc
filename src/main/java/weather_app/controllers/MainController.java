package weather_app.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import weather_app.dto.weather.WeatherResponseDTO;
import weather_app.services.WeatherService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final WeatherService weatherService;

    @GetMapping(value = "/")
    public String getLoginPage(@RequestAttribute("userId") String userId, Model model) {
        List<WeatherResponseDTO> weather = weatherService.getWeatherResponsesDTO(userId);
        model.addAttribute("weather", weather);
        return "index";
    }
}