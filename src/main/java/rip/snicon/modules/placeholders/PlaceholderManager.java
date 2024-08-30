package rip.snicon.modules.placeholders;

import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderManager {

    private static Map<Player, Map<String, String>> playerPlaceholders = new HashMap<>();

    public static Map<Player, Map<String, String>> getPlayerPlaceholders() {
        return playerPlaceholders;
    }


    public static void addPlaceholdersToPlayer(Player player, Map<String, String> placeholderMap) {
        playerPlaceholders.put(player, placeholderMap);
    }

    public static void setPlaceholderToPlayer(Player player, String placeholder, String value) {
        // Retrieve the existing placeholder map for the player, or create a new one if it doesn't exist
        Map<String, String> placeholderMap = playerPlaceholders.getOrDefault(player, new HashMap<>());

        // Update the placeholder value
        placeholderMap.put(placeholder, value);

        // Put the updated map back into the playerPlaceholders map
        playerPlaceholders.put(player, placeholderMap);
    }

    public static String getPlaceholderForPlayer(Player player, String placeholder) {
        // Retrieve the existing placeholder map for the player, or create a new one if it doesn't exist
        Map<String, String> placeholderMap = playerPlaceholders.getOrDefault(player, new HashMap<>());

        // return the value
        return placeholderMap.get(placeholder);

    }



}
