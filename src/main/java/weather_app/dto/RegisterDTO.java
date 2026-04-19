package weather_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterDTO {
    @Size(min = 4, max = 16, message = "Username should contain between 4 and 16 characters.")
    @NotBlank(message = "Заполнение поля 'name' обязательно")
    private String username;

    @NotBlank(message = "Заполнение поля 'пароль' обязательно")
    private String password;

    @NotBlank(message = "Заполнение поля 'повторите пароль' обязательно")
    private String repeatedPassword;
}