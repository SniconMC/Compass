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

            });
        });
    }
}

