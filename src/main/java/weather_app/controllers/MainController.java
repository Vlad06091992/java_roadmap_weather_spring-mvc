package weather_app.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import weather_app.dto.weather.WeatherResponseDTO;
import weather_app.entities.User;
import weather_app.services.UserService;
import weather_app.services.WeatherService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final WeatherService weatherService;
    private final UserService userService;

    @GetMapping(value = "/")
    public String getLoginPage(@RequestAttribute("userId") String userId, Model model) {
        List<WeatherResponseDTO> weather = weatherService.getWeatherResponsesDTO(userId);
        model.addAttribute("weather", weather);
        Optional<User> user = userService.findUserById(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        }
        return "index";
    }
}