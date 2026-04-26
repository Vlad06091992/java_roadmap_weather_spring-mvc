package weather_app.interceptors;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import weather_app.components.CookieManager;
import weather_app.exceptions.NotAuthorizedException;
import weather_app.services.AuthService;

@Slf4j
@AllArgsConstructor
@Component
public class SessionInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager;
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("preHandle");
        Cookie[] cookies = request.getCookies();

        Cookie sessionCookie = cookieManager
                .getCookieByName(cookies, "session_id")
                .orElseThrow(NotAuthorizedException::new);
        boolean isValid = authService.isValidSession(sessionCookie);

        if (!isValid) {
            throw new NotAuthorizedException();
        }
        return true;
    }
}
