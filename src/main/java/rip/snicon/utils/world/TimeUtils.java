package rip.snicon.utils.world;

import rip.snicon.Main;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for converting time representations into a corresponding world time in ticks.
 *
 * <p>This class provides a method to convert human-readable time names (like "morning", "noon", "midnight")
 * or numerical string representations of time into Minecraft's internal time format, measured in ticks.</p>
 *
 * <p>Minecraft time is measured in ticks, where one day in the game equals 24,000 ticks.
 * The method allows for both named times and custom tick values.</p>
 *
 * @author znopp
 */
public class TimeUtils {


    /**
     * Converts a given time name or numerical string into a Minecraft world time in ticks.
     *
     * <p>This method supports predefined time names ("morning", "day", "noon", "night", "midnight")
     * and converts them into their corresponding tick values:
     * <ul>
     *   <li>"morning" - 0 ticks</li>
     *   <li>"day" - 1000 ticks</li>
     *   <li>"noon" - 6000 ticks</li>
     *   <li>"night" - 13000 ticks</li>
     *   <li>"midnight" - 18000 ticks</li>
     * </ul>
     *
     * <p>If the input is a numeric string, it is parsed directly into a tick value. If the input is
     * invalid or not recognized, a default value of 6000 ticks (corresponding to "noon") is returned.</p>
     *
     * @param name A string representing either a named time ("morning", "day", "noon", "night", "midnight")
     *             or a numeric string representing ticks.
     * @return A long value representing the corresponding time in ticks. If the input is invalid,
     *         defaults to 6000 ticks.
     *
     * @author znopp
     */
    public static long convertTime(String name) {

        List<String> timeNames = Arrays.asList("morning", "day", "noon", "night", "midnight");

        if (timeNames.contains(name)) {
            return switch (name.toLowerCase()) {
                case "morning" -> 0;
                case "day" -> 1000;
                case "night" -> 13000;
                case "midnight" -> 18000;
                default -> 6000;
            };
        }

        try {
            return Long.parseLong(name);
        } catch (NumberFormatException e) {
            Main.logger.warn("Invalid number, or malformed word: " + e);
            Main.logger.warn("Defaulting to noon (6000)");
            return 6000;
        }
    }

}
