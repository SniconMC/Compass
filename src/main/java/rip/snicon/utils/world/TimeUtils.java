package rip.snicon.utils.world;

import rip.snicon.Main;

import java.util.Arrays;
import java.util.List;

public class TimeUtils {

    public static long convertTime(String name) {

        List<String> timeNames = Arrays.asList("morning", "day", "noon", "night", "midnight");

        if (timeNames.contains(name)) {
            return switch (name.toLowerCase()) {
                default -> 6000;
                case "morning" -> 0;
                case "day" -> 1000;
                case "night" -> 13000;
                case "midnight" -> 18000;
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
