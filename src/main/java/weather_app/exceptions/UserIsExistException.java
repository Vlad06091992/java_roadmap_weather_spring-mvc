package weather_app.exceptions;

public class UserIsExistException extends RuntimeException {

    public UserIsExistException() {
        super("User already exist");
    }
}
