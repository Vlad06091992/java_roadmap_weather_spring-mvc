package weather_app.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import weather_app.dto.location.LocationDTO;
import weather_app.entities.User;
import weather_app.entities.UserLocation;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LocationsDao {
    private final SessionFactory sessionFactory;
    public void addLocation(LocationDTO locationDTO, String userId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            UserLocation userLocation = new UserLocation();
            User user = session.getReference(User.class, userId);
            userLocation.setUser(user);
            userLocation.setLatitude(locationDTO.getLatitude());
            userLocation.setLongitude(locationDTO.getLongitude());
            userLocation.setName(locationDTO.getName());
            session.persist(userLocation);
        } catch (Exception ex) {
            if (ex instanceof org.hibernate.exception.ConstraintViolationException) {
                log.error("Location already exists for user {}", userId);
            } else {
                log.error("Unexpected error adding location for user {}: {}", userId, ex.getMessage(), ex);
                throw new RuntimeException("Failed to add location", ex);
            }
        }
    }

    public void deleteLocation(LocationDTO locationDTO, String userId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery("DELETE FROM public.user_locations\n" +
                        "WHERE ABS(latitude - :latitude) < 0.00005\n" +
                        "  AND ABS(longitude - :longitude) < 0.00005\n" +
                        "  AND user_id = :userId")
                .setParameter("userId", Long.parseLong(userId))
                .setParameter("latitude", locationDTO.getLatitude())
                .setParameter("longitude", locationDTO.getLongitude())
                .executeUpdate();
    }

    public List<UserLocation> getLocations(String userId) {
        Session session = sessionFactory.getCurrentSession();
        List<UserLocation> userLocations = session
                .createQuery("from UserLocation where user.id = :userId order by id desc")
                .setParameter("userId", userId)
                .getResultList();

        return userLocations;
    }

}
