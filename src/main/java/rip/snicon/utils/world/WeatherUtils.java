package rip.snicon.utils.world;

import net.minestom.server.instance.Weather;

public class WeatherUtils {

    public static Weather convertWeather(String name){

        return switch (name.toLowerCase()){
            default -> Weather.CLEAR;
            case "sun", "clear" -> Weather.CLEAR;
            case "rain" -> Weather.RAIN;
            case "thunderstorm", "thunder", "storm", "stormy" -> Weather.THUNDER;
        };
    }
}
