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
            if (event.getPlayer() instanceof MysteryPlayer sender) {
                // Fetch player details

                String message = event.getRawMessage();

                // Convert to component and broadcast
                event.setCancelled(true);
                // Send a personalized message to each player
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(receiver -> {
                    if (receiver instanceof MysteryPlayer mysteryReceiver) {
                        // Determine if the receiver wants to see professionDisplay
                        // Format the message
                        String formattedMessage = String.format(
                                "<dark_gray>[<gray>%s</gray>]</dark_gray> %s: <gray>%s</gray>",
                                mysteryReceiver.getProfessionDisplay(),
                                sender.getRankDisplayName(),
                                message
                        );

                        // Send the customized message
                        receiver.sendMessage(TextUtils.convertStringToComponent(formattedMessage));
                    }
                });
            }
        });

        // Player Spawn Event Listener
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            Player joiningPlayer = event.getPlayer();
            if (joiningPlayer instanceof MysteryPlayer sender) {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(receiver -> {
                    if (receiver != joiningPlayer && receiver instanceof MysteryPlayer mysteryReceiver) {
                        // Format the message
                        String formattedMessage = String.format(
                                "<dark_gray>[<green>+</green>]</dark_gray> <dark_gray>[<gray>%s</gray>]</dark_gray> %s",
                                mysteryReceiver.getProfessionDisplay(),
                                sender.getRankDisplayName()
                        );

                        // Send the customized message
                        receiver.sendMessage(TextUtils.convertStringToComponent(formattedMessage));
                    }
                });
            }
        });

        // Player Disconnect Event Listener
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            Player leavingPlayer = event.getPlayer();
            if (leavingPlayer instanceof MysteryPlayer sender) {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(receiver -> {
                    if (receiver != leavingPlayer && receiver instanceof MysteryPlayer mysteryReceiver) {
                        // Format the message
                        String formattedMessage = String.format(
                                "<dark_gray>[<red>-</red>]</dark_gray> <dark_gray>[<gray>%s</gray>]</dark_gray> %s",
                                mysteryReceiver.getProfessionDisplay(),
                                sender.getRankDisplayName()
                        );

                        // Send the customized message
                        receiver.sendMessage(TextUtils.convertStringToComponent(formattedMessage));
                    }
                });
            }
        });
    }

}
