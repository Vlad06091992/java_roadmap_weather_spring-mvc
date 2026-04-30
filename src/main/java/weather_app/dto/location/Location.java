package weather_app.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    String name;
    @JsonProperty("lat")
    float latitude;
    @JsonProperty("lon")
    float longitude;
    String country;
    String state;
}
