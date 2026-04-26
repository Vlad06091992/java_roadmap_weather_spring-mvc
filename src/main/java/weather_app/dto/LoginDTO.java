package weather_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import weather_app.annotations.EqualsPasswordFields;


@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "Please enter your username.")
    private String username;

    @NotBlank(message = "Please enter your password.")
    private String password;
}