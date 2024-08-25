package rip.snicon.modules.placeholders;

import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderManager {

    private static Map<Player, Map<String, String>> playerPlaceholders = new HashMap<>();
    private static final Map<String, String> placeholderMap = new HashMap<>();
    public static Map<Player, Map<String, String>> getPlayerPlaceholders() {
        return playerPlaceholders;
    }


    public static void addPlaceholdersToPlayer(Player player, Map<String, String> placeholderMap) {
        playerPlaceholders.put(player, placeholderMap);
    }

    public static void setPlaceholderToPlayer(Player player, String placeholder, String value) {
        placeholderMap.put(placeholder, value);
        PlaceholderManager.playerPlaceholders.put(player, placeholderMap);
    }



}
