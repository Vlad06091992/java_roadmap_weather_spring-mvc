package weather_app.exceptions;

public class UserIsExistException extends RuntimeException {

    public UserIsExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
