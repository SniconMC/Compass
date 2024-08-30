package rip.snicon.utils.world;

import net.minestom.server.instance.Weather;
import rip.snicon.Main;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for converting string representations of weather types into corresponding
 * {@link Weather} enums used in the game environment.
 *
 * <p>This class provides a method to convert various string inputs, such as "sun", "rain", or "thunderstorm",
 * into their appropriate {@link Weather} enum value. It is useful for interpreting user input or configuration
 * files that specify weather conditions by name.</p>
 *
 * @author znopp
 */
public class WeatherUtils {

    /**
     * Converts a given weather name string into its corresponding {@link Weather} enum.
     *
     * <p>This method supports several weather names and maps them to the {@link Weather} enums:
     * <ul>
     *   <li>"sun" or "clear" - {@link Weather#CLEAR}</li>
     *   <li>"rain" - {@link Weather#RAIN}</li>
     *   <li>"thunderstorm", "thunder", "storm", or "stormy" - {@link Weather#THUNDER}</li>
     * </ul>
     *
     * <p>If the input string does not match any recognized weather name, the method defaults to {@link Weather#CLEAR}.</p>
     *
     * @param name A string representing the weather name (e.g., "clear", "rain", "thunderstorm").
     * @return The corresponding {@link Weather} enum. If the input is not recognized, defaults to {@link Weather#CLEAR}.
     *
     * @author znopp
     */
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
