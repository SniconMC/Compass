package rip.snicon.compass.listeners.worlds;

import com.github.sniconmc.container.creators.HotbarCreator;
import com.github.sniconmc.sidebar.SidebarManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import rip.snicon.compass.instances.InstanceCreator;
import rip.snicon.compass.instances.worlds.WorldInfo;
import rip.snicon.compass.listeners.VoidListener;

import java.util.Set;

public class AFK {

    private final EventNode<PlayerEvent> hubNode;

    public AFK(EventNode<Event> node) {
        this.hubNode = EventNode.value("afk", EventFilter.PLAYER, player -> player.getInstance() == InstanceCreator.getInstance().getInstanceByWorldName("afk"));
        onPlayerJoin();
        onPlayerQuit();
        onPlayerMove();
        node.addChild(hubNode);

    }

    public void onPlayerJoin() {
        hubNode.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            Instance playerInstance = player.getInstance();
            Set<Player> players = playerInstance.getPlayers();
            for (Player onlinePlayer : players) {
                SidebarManager.setSidebar(player, "hub_sidebar");
                HotbarCreator.setHotbar(player, "lobby");

                onlinePlayer.sendMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[<green>+</green>]</dark_gray> " + player.getUsername()));

            }

            Instance hubInstance = InstanceCreator.getInstance().getInstanceByWorldName("hub");
            Set<Player> hubPlayers = hubInstance.getPlayers();
            for (Player onlinePlayer : hubPlayers) {
                SidebarManager.setSidebar(onlinePlayer, "hub_sidebar");
                HotbarCreator.setHotbar(onlinePlayer, "lobby");

                onlinePlayer.sendMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[<red>-</red>]</dark_gray> " + player.getUsername()));
            }
        });
    }

    public void onPlayerQuit() {
        hubNode.addListener(PlayerDisconnectEvent.class, event -> {
            Player player = event.getPlayer();
            Instance playerInstance = player.getInstance();
            Set<Player> players = playerInstance.getPlayers();
            for (Player onlinePlayer : players) {

                onlinePlayer.sendMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[<red>-</red>]</dark_gray> " + player.getUsername()));
            }
        });
    }

    public void onPlayerMove() {
        hubNode.addListener(PlayerMoveEvent.class, event -> new VoidListener().onVoidLimit(event));
    }

}
