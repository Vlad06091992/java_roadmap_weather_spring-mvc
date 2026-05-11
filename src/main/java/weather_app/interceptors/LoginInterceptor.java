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

import java.io.IOException;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager;
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Cookie[] cookies = request.getCookies();

        Optional<Cookie> sessionCookie = cookieManager
                .getCookieByName(cookies, "session_id");

        if (sessionCookie.isPresent()) {
        Cookie cookie = sessionCookie.get();
            Optional<Long> userId = authService.getAuthorizedUserId(cookie);
            if (!userId.isEmpty()) {
                response.sendRedirect("/");
                return false;
            } else {
                return true;
            }
        } else {
            return  true;
        }

    }
}
