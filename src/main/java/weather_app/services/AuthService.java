package weather_app.services;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@Slf4j
public class AuthService {

    @Value("${session-duration-hours}")
    private int sessionDuration;

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

    public Cookie setCredentials(Long userId) {
        UserSession userSession = createSession(userId);
        return cookieManager.createSessionCookie(userSession.getId());
    }

    public Optional<Long> getAuthorizedUserId(Cookie cookie) {
        String uuid = cookie.getValue();
        Optional<UserSession> userSession = userSessionDao
                .getUserSessionById(uuid);

        if (userSession.isEmpty()) return Optional.empty();

        OffsetDateTime expiresAr = userSession.get().getExpiresAt();
        long userId = userSession.get().getUser().getId();
        OffsetDateTime now = OffsetDateTime.now();

        if (expiresAr.isBefore(now)) {
            return Optional.empty();
        } else {
            return Optional.of(userId);
        }
    }

    public void removeExpiredSessions() {
        userSessionDao.removeExpiredSessions();
    }

    public Cookie logout(Cookie[] cookies, String userId) {
        Cookie sessionCookie = cookieManager
                .getCookieByName(cookies, "session_id")
                .orElseThrow(NotAuthorizedException::new);

        userSessionDao.removeUserSessions(userId);
        Cookie invalidatedSessionCookie = cookieManager.invalidateCookie(sessionCookie);
        return invalidatedSessionCookie;
    }


    private UserSession createSession(Long userId) {
//        TemporalAmount period = Period.ofDays(10);
//        TemporalAmount duration = Duration.ofSeconds(45);
        TemporalAmount duration = Duration.ofHours(sessionDuration);
        UserSession userSession = userSessionDao.addSession(userId, duration);
        return userSession;
    }


}
