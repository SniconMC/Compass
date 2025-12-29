package rip.snicon.compass.content.bundlebot;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BundleBot {

    private static final Set<UUID> playersInRegion = new HashSet<>();
    private static BundleBotInteractionEntity entity;
    private static BundleBotText textDisplay;



    public static void enter(MysteryPlayer player) {
        UUID playerId = player.getUuid();
        playersInRegion.add(playerId);

        // Create entity if not present
        if (entity == null) {
            entity = new BundleBotInteractionEntity();
        }
        /*
        // Initialize text display
        if (textDisplay == null || textDisplay.isEmpty()) {
            textDisplay = new BundleBotText(player);
        }

        // Spawn text for the player
        textDisplay.spawn(player);

         */
    }

    public static void leave(MysteryPlayer player) {
        UUID playerId = player.getUuid();
        playersInRegion.remove(playerId);

        if (playersInRegion.isEmpty()) {
            if (entity != null) {
                entity.remove();
                entity = null;
            }

            if (textDisplay != null) {
                textDisplay = null;
            }
        }
    }

    public static boolean isInRegion(MysteryPlayer player) {
        return playersInRegion.contains(player.getUuid());
    }

    public static Entity getEntity() {
        return entity;
    }
}
