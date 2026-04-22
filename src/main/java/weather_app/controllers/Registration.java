package weather_app.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import weather_app.components.ViewErrorHandler;
import weather_app.dto.RegisterDTO;
import weather_app.services.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class Registration {
    private final ViewErrorHandler viewErrorHandler;
    private final UserService userService ;

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid RegisterDTO registerDTO, BindingResult bindingResult, Model model) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        if (bindingResult.hasErrors()) {
            log.info("Validation errors found {}", bindingResult.getFieldErrors());
            viewErrorHandler.addErrorsToModel(errorList, model);
            return "register";
        } else {
            log.info("user succefully created {}", registerDTO);
            //создать юзера, куда-то редиректунть
            userService.addUser(registerDTO.getUsername(), registerDTO.getPassword());
            System.out.println(errorList);
            return "register";
        }
    }

    @GetMapping(value = "/register")
    public String getRegisterPage() {
        return "register";
    }
}