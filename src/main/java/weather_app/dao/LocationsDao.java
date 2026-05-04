package weather_app.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import weather_app.dto.location.LocationDTO;
import weather_app.entities.User;
import weather_app.entities.UserLocation;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LocationsDao {
    private final SessionFactory sessionFactory;
    public UserLocation addLocation(LocationDTO locationDTO, String userId) {
        Session session = sessionFactory.getCurrentSession();
        UserLocation userLocation = new UserLocation();
        User user = session.getReference(User.class, userId);
        userLocation.setUser(user);
        userLocation.setLatitude(locationDTO.getLatitude());
        userLocation.setLongitude(locationDTO.getLongitude());
        userLocation.setName(locationDTO.getName());
        session.persist(userLocation);

        return userLocation;
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
