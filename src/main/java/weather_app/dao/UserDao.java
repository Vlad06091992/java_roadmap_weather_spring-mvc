package weather_app.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import weather_app.entities.User;

import java.util.List;

@Repository
public class UserDao {
    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public User addUser(String username, String passwordHash) {
        Session session = sessionFactory.getCurrentSession();
        User user = new User();
        user.setLogin(username);
        user.setPassword(passwordHash);
        session.persist(user);
        return user;
    }


}