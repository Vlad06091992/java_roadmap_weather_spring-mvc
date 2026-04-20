package weather_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import weather_app.annotations.EqualsPasswordFields;


@Getter
@Setter
@EqualsPasswordFields
public class RegisterDTO {
    @NotBlank(message = "Please enter your username.")
    @Size(min = 4, max = 16, message = "Username should contain between 4 and 16 characters.")
    @Pattern(regexp = "^[A-Za-z0-9]+", message = "Username should contain only english letters or digits.")
    private String username;

    @NotBlank(message = "Please enter your password.")
    @Size(min = 4, max = 20, message = "Password should contain between 4 and 20 characters.")
    @Pattern(regexp = "^[A-Za-z0-9]+", message = "Password should contain only english letters or digits.")

    private String password;

    @NotBlank(message = "Please repeat your password.")
    private String repeatedPassword;
}