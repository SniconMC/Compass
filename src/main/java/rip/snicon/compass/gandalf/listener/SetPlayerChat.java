package rip.snicon.compass.gandalf.listener;

import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.text.ColorUtils;
import com.github.sniconmc.utils.text.TextUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import rip.snicon.compass.Main;
import rip.snicon.compass.gandalf.GandalfManager;
import rip.snicon.compass.gandalf.config.GandalfProfile;
import rip.snicon.compass.gandalf.config.GandalfRank;
import rip.snicon.compass.gandalf.utils.ChatUtils;

import java.util.List;
import java.util.Random;

public class SetPlayerChat {

    public SetPlayerChat(){
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
            Player sender = event.getPlayer();

            for (Player viewer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                ChatUtils.sendChatMessage(viewer, sender, event);
            }

        });
    }
}
