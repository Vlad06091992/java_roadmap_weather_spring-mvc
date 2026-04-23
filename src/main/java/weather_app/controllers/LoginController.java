package weather_app.controllers;

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
import weather_app.exceptions.IncorrectLoginDataEcxeption;
import weather_app.services.AuthService;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final ViewErrorHandler viewErrorHandler;
    private final AuthService authService ;

    @PostMapping("/login")
    public String register(@ModelAttribute @Valid LoginDTO loginDTO, BindingResult bindingResult, Model model) throws IncorrectLoginDataEcxeption {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        if (bindingResult.hasErrors()) {
            log.info("Validation errors found {}", bindingResult.getFieldErrors());
            viewErrorHandler.addErrorsToModel(errorList, model);
            return "login";
        } else {
            //залогинить юзера, сделать редирект на главную
            authService.loginUser(loginDTO.getUsername(), loginDTO.getPassword());
            return "redirect:/";
        }
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }
}