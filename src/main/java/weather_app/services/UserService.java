package weather_app.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weather_app.dao.UserDao;
import weather_app.entities.User;
import weather_app.exceptions.UserIsExistException;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserDao dao;

    public User addUser(String login, String password) {
        try {
            String passwordHash = passwordEncoder.encode(password);
            return dao.addUser(login, passwordHash);
        } catch (ConstraintViolationException ex) {
            throw new UserIsExistException();
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }
}
