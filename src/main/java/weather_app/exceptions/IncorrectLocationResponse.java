package weather_app.exceptions;

public class IncorrectLocationResponse extends RuntimeException {
    public IncorrectLocationResponse() {
        super("invalid response");
    }
}
