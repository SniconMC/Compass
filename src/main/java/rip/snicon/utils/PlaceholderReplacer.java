package rip.snicon.utils;

import net.minestom.server.entity.Player;
import rip.snicon.modules.placeholders.PlaceholderManager;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PlaceholderReplacer {

    public static String replacePlaceholders(Player player, String text) {
        // Handle null or empty text
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Get player-specific placeholders
        Map<String, String> placeholders = PlaceholderManager.getPlayerPlaceholders().get(player);
        if (placeholders == null || placeholders.isEmpty()) {
            return text;
        }

        // Regex pattern to match placeholders like $(placeholder_name)
        Pattern pattern = Pattern.compile("\\$\\((.*?)\\)");
        Matcher matcher = pattern.matcher(text);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String placeholderName = matcher.group(1);
            String replacement = placeholders.getOrDefault(placeholderName, matcher.group());

            // Handle special characters in replacement
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }


    // Method to check if the text contains any placeholders
    public static boolean containsPlaceholders(String text) {
        // Regex pattern to match placeholders like $(placeholder_name)
        Pattern pattern = Pattern.compile("\\$\\((.*?)\\)");
        Matcher matcher = pattern.matcher(text);

        // Return true if any placeholders are found
        return matcher.find();
    }
}
