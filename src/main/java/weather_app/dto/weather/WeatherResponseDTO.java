package weather_app.dto.weather;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WeatherResponseDTO {

    // страна в табличке
    String country;
    // название населенного пункта в табличке
    String name;
    // определяет иконку в табличке
    String main;

    // влажность в табличке
    byte humidity;

    // описание, например Broken clouds/Clear sky
    String description;

    // префикс для иконки, например 04d
    String icon;

    // температура ощущается как в кельвинах
    float kelvinFeelsLikeTemperature;


    // температура по факту в кельвинах
    float kelvinTemperature;

    // температура в цельсиях(как ощущается) для view
    public int getCelsiusFeelsLikeTemperature() {
        return Math.round((float) (kelvinFeelsLikeTemperature - 273.15));
    }

    // температура в цельсиях(по факту) для view
    public int getCelsiusTemperature() {
        return Math.round((float) (kelvinTemperature - 273.15));
    }

    // иконка погоды
    public String getWeatherIconUrl() {
        return "https://openweathermap.org/payload/api/media/file/" + icon + ".png";
    }

    public String getDescription() {
        return description.substring(0, 1).toUpperCase() + description.substring(1);
    }


    // для кеширования
    @EqualsAndHashCode.Exclude
    public float lon;
    // для кеширования
    @EqualsAndHashCode.Exclude
    public float lat;
}
