package rip.snicon.compass.gandalf.listener;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import rip.snicon.compass.gandalf.utils.TabUtils;


public class SetPlayerTab {

    public SetPlayerTab() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                Player joinedPlayer = event.getPlayer();

                for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    TabUtils.setExistingPlayer(onlinePlayer, joinedPlayer);
                }
            });
        });
    }
}

