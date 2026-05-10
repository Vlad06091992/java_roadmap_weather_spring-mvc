package weather_app.services;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import weather_app.configs.TestConfig;
import weather_app.entities.User;
import weather_app.exceptions.IncorrectLoginDataException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void cleanDatabase() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("DELETE FROM sessions");
            conn.createStatement().execute("DELETE FROM users");
        }
    }

    @Test
    void loginUser_withValidCredentials_returnsUser() {
        userService.addUser("testuser", "password123");
        User user = authService.loginUser("testuser", "password123");

        assertNotNull(user);
        assertEquals("testuser", user.getLogin());
    }

    @Test
    void loginUser_withWrongPassword_throwsIncorrectLoginDataException() {
        userService.addUser("testuser", "password123");

        assertThrows(IncorrectLoginDataException.class,
                () -> authService.loginUser("testuser", "wrongpassword"));
    }

    @Test
    void loginUser_withNonExistentUser_throwsIncorrectLoginDataException() {
        assertThrows(IncorrectLoginDataException.class,
                () -> authService.loginUser("nobody", "anypass"));
    }

    @Test
    void setCredentials_createsSessionInDatabase() throws Exception {
        User user = userService.addUser("testuser", "password123");

        authService.setCredentials(user.getId());

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM sessions WHERE user_id = ?")) {
            ps.setLong(1, user.getId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void setCredentials_returnsCookieWithSessionId() {
        User user = userService.addUser("testuser", "password123");

        Cookie cookie = authService.setCredentials(user.getId());

        assertNotNull(cookie);
        assertEquals("session_id", cookie.getName());
        assertDoesNotThrow(() -> UUID.fromString(cookie.getValue()));
    }

    @Test
    void getAuthorizedUserId_withValidSession_returnsUserId() {
        User user = userService.addUser("testuser", "password123");
        Cookie cookie = authService.setCredentials(user.getId());

        Optional<Long> userId = authService.getAuthorizedUserId(cookie);

        assertTrue(userId.isPresent());
        assertEquals(user.getId(), userId.get());
    }

    @Test
    void getAuthorizedUserId_withExpiredSession_returnsEmptyOptional() throws Exception {
        User user = userService.addUser("testuser", "password123");
        UUID sessionId = UUID.randomUUID();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO sessions (id, user_id, expires_at) VALUES (?, ?, ?)")) {
            ps.setObject(1, sessionId);
            ps.setLong(2, user.getId());
            ps.setObject(3, OffsetDateTime.now().minusHours(2));
            ps.executeUpdate();
        }

        Cookie cookie = new Cookie("session_id", sessionId.toString());
        Optional<Long> userId = authService.getAuthorizedUserId(cookie);

        assertTrue(userId.isEmpty());
    }

    @Test
    void removeExpiredSessions_removesExpiredSessionsFromDatabase() throws Exception {
        User user = userService.addUser("testuser", "password123");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO sessions (id, user_id, expires_at) VALUES (?, ?, ?)")) {
            ps.setObject(1, UUID.randomUUID());
            ps.setLong(2, user.getId());
            ps.setObject(3, OffsetDateTime.now().minusHours(2));
            ps.executeUpdate();
        }

        authService.removeExpiredSessions();

        try (Connection conn = dataSource.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(
                     "SELECT COUNT(*) FROM sessions WHERE expires_at < NOW()")) {
            rs.next();
            assertEquals(0, rs.getInt(1));
        }
    }
}
