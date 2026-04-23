package weather_app.exceptions;

public class IncorrectLoginDataEcxeption extends RuntimeException {
    public IncorrectLoginDataEcxeption() {
        super("Check Password or login");
    }
}
