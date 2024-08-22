package rip.snicon.modules.placeholders;

import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlaceHolder {

    private static Map<Player, Map<String, String>> playerPlaceholders = new HashMap<>();
    private static final Map<String, String> placeholderMap = new HashMap<>();
    public static Map<Player, Map<String, String>> getPlayerPlaceholders() {
        return playerPlaceholders;
    }

    public static void setPlayerPlaceholders(Map<Player, Map<String, String>> playerPlaceholders) {
        PlaceHolder.playerPlaceholders = playerPlaceholders;
    }

    public static void setPlayerPlaceholders(Player player, String placeholder, String value) {
        placeholderMap.put(placeholder, value);
        PlaceHolder.playerPlaceholders.put(player, placeholderMap);
    }



}
