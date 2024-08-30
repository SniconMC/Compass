package rip.snicon.utils.world;

import net.minestom.server.instance.Weather;
import rip.snicon.Main;

import java.util.Arrays;
import java.util.List;

public class WeatherUtils {

    public static Weather convertWeather(String name){

        List<String> weatherNames = Arrays.asList("rain", "thunderstorm", "clear");

        if (weatherNames.contains(name)){
            return switch (name.toLowerCase()){
                case "rain" -> Weather.RAIN;
                case "thunderstorm", "thunder", "storm", "stormy" -> Weather.THUNDER;
                default -> Weather.CLEAR;
            };
        } else {
            Main.logger.warn("Weather " + name + " not found!");
            Main.logger.warn("Defaulting to CLEAR");
            return Weather.CLEAR;
        }

    }
}
