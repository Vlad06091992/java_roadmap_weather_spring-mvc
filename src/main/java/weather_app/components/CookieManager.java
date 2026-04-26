package weather_app.components;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class CookieManager {

    public Cookie createSessionCookie(UUID sessionId) {
        Cookie cookie = new Cookie("session_id", sessionId.toString());

        cookie.setMaxAge(7 * 24 * 60 * 60); // Expires in 7 days
        cookie.setHttpOnly(true);          // Accessible only by server (prevents XSS)
        cookie.setSecure(true);            // Send only over HTTPS
        cookie.setPath("/");               // Available for all application paths

        return cookie;
    }

    public Optional<Cookie> getCookieByName(Cookie[] cookies, String cookieName) {

        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }

        Cookie cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(cookie);

    }
}
