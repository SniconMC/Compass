package rip.snicon.compass.utils;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import rip.snicon.compass.player.MysteryPlayer;

public class TabUtils {

    public static void setPlayerTab(MysteryPlayer player) {
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            // Set tab header and footer for the new player
            Component header = TextUtils.convertStringToComponent("<gradient:dark_purple:light_purple><bold>Multiverse Mystery</bold></gradient>");
            Component footer = TextUtils.convertStringToComponent("<yellow>www.fuckyouhängdigsjälv.net</yellow>");
            player.sendPlayerListHeaderAndFooter(header, footer);

            // Update tablist entries for the new player
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(onlinePlayer -> {
                if (onlinePlayer instanceof MysteryPlayer mysteryPlayer) {
                    mysteryPlayer.updateDisplayName(); // Update display for this player
                    player.updateDisplayName(); // Update display for the joining player
                }
            });
        });
    }
}
