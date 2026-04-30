package weather_app.dto.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {
    String name;
    float latitude;
    float longitude;
}
