package weather_app.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weather_app.dao.UserDao;
import weather_app.entities.User;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao dao;

    public User addUser(String login, String password){
        String passwordHash = passwordEncoder.encode(password);
        return dao.addUser(login,passwordHash);
    }
}
