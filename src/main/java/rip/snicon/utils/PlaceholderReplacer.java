package rip.snicon.utils;

import net.minestom.server.entity.Player;
import rip.snicon.modules.placeholders.PlaceholderManager;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utility class for replacing placeholders in strings with player-specific data.
 *
 * <p>This class provides methods to dynamically replace placeholders in strings with
 * corresponding values based on player-specific information. Placeholders are defined
 * using the format <code>$(placeholder_name)</code>.</p>
 *
 * <p>The replacement data is fetched from a placeholder manager, which maintains
 * a mapping of player-specific placeholders and their corresponding values.</p>
 *
 * @see Player
 * @see PlaceholderManager
 * @see Pattern
 * @see Matcher
 * @see <a href="https://javadoc.minestom.net/">Minestom Documentation</a>
 *
 * @author Wi1helm
 * @author znopp
 */
public class PlaceholderReplacer {

    /**
     * Replaces placeholders in the provided text with player-specific values.
     *
     * <p>Placeholders are expected to be in the format <code>$(placeholder_name)</code>.
     * If a placeholder does not have a corresponding value, it is left unchanged.</p>
     *
     * <p>If the provided text is <code>null</code> or empty, the method returns it unchanged.</p>
     *
     * @param player The {@link Player} whose placeholder values are to be used for replacement.
     * @param text The text containing placeholders to be replaced.
     * @return The text with placeholders replaced by their corresponding values, or the original text if no replacements are found.
     */
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


    /**
     * Checks if the provided text contains any placeholders.
     *
     * <p>A placeholder is defined as any substring that matches the pattern <code>$(placeholder_name)</code>.</p>
     *
     * @param text The text to check for placeholders.
     * @return <code>true</code> if the text contains any placeholders, <code>false</code> otherwise.
     *
     * @author Wi1helm
     */
    public static boolean containsPlaceholders(String text) {
        // Regex pattern to match placeholders like $(placeholder_name)
        Pattern pattern = Pattern.compile("\\$\\((.*?)\\)");
        Matcher matcher = pattern.matcher(text);

        // Return true if any placeholders are found
        return matcher.find();
    }
}
