package weather_app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import weather_app.configs.TestConfig;
import weather_app.entities.User;
import weather_app.exceptions.UserIsExistException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class UserServiceTest {

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
    void addUser_savesUserToDatabase() throws Exception {
        userService.addUser("testuser", "password123");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM users WHERE login = ?")) {
            ps.setString(1, "testuser");
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void addUser_returnsUserWithGeneratedId() {
        User user = userService.addUser("newuser", "secret");

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("newuser", user.getLogin());
    }

    @Test
    void addUser_hashesPassword() {
        User user = userService.addUser("hashuser", "plainpassword");

        assertNotEquals("plainpassword", user.getPassword());
        assertTrue(user.getPassword().startsWith("$2a$"));
    }

    @Test
    void addUser_withDuplicateLogin_throwsUserIsExistException() {
        userService.addUser("dupuser", "pass1");

        assertThrows(UserIsExistException.class,
                () -> userService.addUser("dupuser", "pass2"));
    }
}
