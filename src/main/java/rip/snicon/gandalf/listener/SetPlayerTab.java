package rip.snicon.gandalf.listener;

import com.github.sniconmc.oblivion.OblivionMain;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import rip.snicon.Main;
import rip.snicon.gandalf.utils.TabUtils;

import java.util.List;
import java.util.Objects;


public class SetPlayerTab {

    public SetPlayerTab() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                Player joinedPlayer = event.getPlayer();

                // Remove and then re-add the player to all other players' tabs
                for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {

                    TabUtils.setTabPlayer(player, joinedPlayer, Objects.requireNonNull(joinedPlayer.getSkin()));
                }

                // Update the joined player's tab with all other players
                for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    if (player != joinedPlayer) {
                        TabUtils.setTabPlayer(joinedPlayer, player, Objects.requireNonNull(player.getSkin()), List.of(""), List.of(""));
                    }
                }
            });
        });
    }
}

