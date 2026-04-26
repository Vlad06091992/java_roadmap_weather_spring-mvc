package weather_app.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import weather_app.components.ViewErrorHandler;
import weather_app.dto.LoginDTO;
import weather_app.entities.User;
import weather_app.exceptions.IncorrectLoginDataException;
import weather_app.services.AuthService;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final ViewErrorHandler viewErrorHandler;
    private final AuthService authService ;

    @PostMapping("/login")
    public String register(@ModelAttribute @Valid LoginDTO loginDTO, BindingResult bindingResult, Model model,  HttpServletResponse response) throws IncorrectLoginDataException {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        if (bindingResult.hasErrors()) {
            log.info("Validation errors found {}", bindingResult.getFieldErrors());
            viewErrorHandler.addErrorsToModel(errorList, model);
            return "login";
        } else {
            //залогинить юзера, добавить ему куки, сделать редирект на главную
            User user = authService.loginUser(loginDTO.getUsername(), loginDTO.getPassword());
            authService.setCredentials(response, user.getId());
            return "redirect:/";
        }
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }
}