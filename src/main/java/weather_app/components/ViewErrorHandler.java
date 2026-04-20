package weather_app.components;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class ViewErrorHandler {

    public void addErrorsToModel(List<FieldError> errorList, Model model) {
        errorList.forEach(error -> {
            model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
        });
    }
}
