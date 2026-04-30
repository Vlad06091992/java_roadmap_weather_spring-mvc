package weather_app.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_locations",uniqueConstraints = {
        @UniqueConstraint(name = "user_locations_latitude_longitude_user_id_key", columnNames = {"latitude", "longitude","user_id"})
} )
@Getter
@Setter
@NoArgsConstructor
public class UserLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    float latitude;

    @Column(name = "longitude", nullable = false)
    float longitude;
}