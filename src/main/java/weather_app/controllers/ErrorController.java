package weather_app.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import weather_app.exceptions.IncorrectLoginDataException;
import weather_app.exceptions.NotAuthorizedException;
import weather_app.exceptions.UserIsExistException;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(UserIsExistException.class)
    public String handleUserIsExistException(UserIsExistException ex, Model model) {
        model.addAttribute("userAlreadyExistsError", ex.getMessage());
        return "register";
    }

    @ExceptionHandler(IncorrectLoginDataException.class)
    public String handleIncorrectLoginDataException(IncorrectLoginDataException ex, Model model) {
        model.addAttribute("incorrectLoginDataError", ex.getMessage());
        return "login";
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public String handleNotAuthorizedException(NotAuthorizedException ex, Model model) {
        model.addAttribute("notAuthorizedError", ex.getMessage());
        return "login";
    }
}
