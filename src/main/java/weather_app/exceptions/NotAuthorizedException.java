package weather_app.exceptions;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
        super("Please log in");
    }
}
