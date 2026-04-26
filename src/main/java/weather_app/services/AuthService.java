package weather_app.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weather_app.components.CookieManager;
import weather_app.dao.UserDao;
import weather_app.dao.UserSessionDao;
import weather_app.entities.User;
import weather_app.entities.UserSession;
import weather_app.exceptions.IncorrectLoginDataException;
import weather_app.exceptions.NotAuthorizedException;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final UserSessionDao userSessionDao;
    private final CookieManager cookieManager;

    public User loginUser(String login, String password) {
        User user = userDao
                .findUserByLogin(login)
                .orElseThrow(IncorrectLoginDataException::new);

        boolean isValidPassword = passwordEncoder.matches(password, user.getPassword());

        if (!isValidPassword) {
            throw new IncorrectLoginDataException();
        }
        return user;
    }

    public void setCredentials(HttpServletResponse response, Long userId) {
        UserSession userSession = createSession(userId);
        Cookie cookie = cookieManager.createSessionCookie(userSession.getId());
        response.addCookie(cookie);
    }

    public boolean isValidSession(Cookie cookie) {
        String uuid = cookie.getValue();
        UserSession userSession = userSessionDao
                .getUserSessionById(uuid)
                .orElseThrow(NotAuthorizedException::new);

        OffsetDateTime expiresAr = userSession.getExpiresAt();
        OffsetDateTime now = OffsetDateTime.now();

        if (expiresAr.isBefore(now)) {
            return false;
        } else {
            return true;
        }
    }

    public void removeExpiredSessions(){
        userSessionDao.removeExpiredSessions();
    }



    private UserSession createSession(Long userId) {
        //        TemporalAmount period = Period.ofDays(10);
        TemporalAmount duration = Duration.ofSeconds(45);
        UserSession userSession = userSessionDao.addSession(userId, duration);
        return userSession;
    }
}
