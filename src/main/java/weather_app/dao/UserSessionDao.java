package weather_app.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import weather_app.entities.UserSession;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSessionDao {
    private final SessionFactory sessionFactory;

    public UserSession addSession(Long userId, TemporalAmount expiresAt) {
        Session session = sessionFactory.getCurrentSession();
        UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        OffsetDateTime plus = OffsetDateTime.now().plus(expiresAt);
        userSession.setExpiresAt(plus);
        session.persist(userSession);
        return userSession;
    }

    public Optional<UserSession> getUserSessionById(String uuid) {
        Session session = sessionFactory.getCurrentSession();
        UserSession userSession = session.createQuery(
                        "FROM UserSession u ORDER BY u.expiresAt DESC", UserSession.class)
                .setMaxResults(1)
                .uniqueResult();
        return Optional.ofNullable(userSession);
    }

    public void removeExpiredSessions() {
        Session session = sessionFactory.getCurrentSession();
        session
                .createNativeQuery("DELETE FROM public.sessions s WHERE s.expires_at < NOW()")
                .executeUpdate();
    }
}
