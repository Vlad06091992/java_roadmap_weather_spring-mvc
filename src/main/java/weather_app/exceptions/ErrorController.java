package weather_app.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(UserIsExistException.class)
    public String handleGeneralException(UserIsExistException ex, Model model) {
        model.addAttribute("userAlreadyExistsError", ex.getMessage());
        return "register";
    }
}
