package weather_app.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import weather_app.dto.RegisterDTO;

import java.util.List;

@Controller
public class Registration {
    @PostMapping(value = "/register", produces = "text/html;charset=UTF-8")
    public String register(@ModelAttribute @Valid RegisterDTO registerDTO, BindingResult bindingResult, Model model) {

        List<ObjectError> errorList = bindingResult.getAllErrors();
        System.out.println(errorList);
        return "register";
    }

    @GetMapping(value = "/register", produces = "text/html;charset=UTF-8")
    public String getRegisterPage() {
        return "register";
    }
}