package weather_app.exceptions;

public class IncorrectWeatherResponse extends RuntimeException {
    public IncorrectWeatherResponse() {
        super("invalid response");
    }
}
