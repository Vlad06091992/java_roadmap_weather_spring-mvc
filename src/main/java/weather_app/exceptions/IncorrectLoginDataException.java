package weather_app.exceptions;

public class IncorrectLoginDataException extends RuntimeException {
    public IncorrectLoginDataException() {
        super("Check Password or login");
    }
}
