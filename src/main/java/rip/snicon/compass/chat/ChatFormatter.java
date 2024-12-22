package rip.snicon.compass.chat;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

public class ChatFormatter {

    public static void setup() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                // Fetch player details

                String message = event.getMessage();

                // MiniMessage format
                String formattedMessage = String.format(
                        player.getFullDisplayName() + ": <gray>%s</gray>",
                         message
                );

                // Convert to component and broadcast
                event.setCancelled(true);
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->
                        p.sendMessage(TextUtils.convertStringToComponent(formattedMessage))
                );
            }
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                // Fetch player details

                // MiniMessage format
                String formattedMessage = String.format(
                        "<dark_gray>[<green>+</green>]</dark_gray> %s", player.getFullDisplayName()
                );

                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> {
                        if (p == player) {
                            return;
                        }
                        p.sendMessage(TextUtils.convertStringToComponent(formattedMessage));
            });
            }
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                // Fetch player details

                // MiniMessage format
                String formattedMessage = String.format(
                        "<dark_gray>[<red>-</red>]</dark_gray> %s", player.getFullDisplayName()
                );

                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> {
                    if (p == player) {
                        return;
                    }
                    p.sendMessage(TextUtils.convertStringToComponent(formattedMessage));
                });
            }
        });
    }

}
