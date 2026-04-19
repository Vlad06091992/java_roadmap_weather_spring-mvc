package weather_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {
    @Size(min = 4, max = 16, message = "Username should contain between 4 and 16 characters.")
    @NotBlank(message = "Заполнение поля 'name' обязательно")
    private String username;

    @NotBlank(message = "Заполнение поля 'пароль' обязательно")
    private String password;

    @NotBlank(message = "Заполнение поля 'повторите пароль' обязательно")
    private String repeatedPassword;


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}