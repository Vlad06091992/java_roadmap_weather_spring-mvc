package weather_app.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weather_app.dto.LoginDTO;
import weather_app.exceptions.IncorrectLoginDataException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    @GetMapping("/search")
    public String register(@RequestParam(name = "locationName", required = false) String name, Model model, HttpServletRequest request) throws IncorrectLoginDataException {
        log.info(name);
        return "search";
    }
}
