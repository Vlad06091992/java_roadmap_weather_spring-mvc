package weather_app.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weather_app.dao.UserDao;
import weather_app.entities.User;
import weather_app.exceptions.IncorrectLoginDataEcxeption;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao dao;

    public void loginUser(String login, String password)  {
        Optional<User> user = Optional
                .ofNullable(dao
                        .findUserByLogin(login)
                        .orElseThrow(IncorrectLoginDataEcxeption::new));
        boolean isValidPassword = passwordEncoder.matches(password, user.get().getPassword());

        if (!isValidPassword) {
            throw new IncorrectLoginDataEcxeption();
        }
    }
}
