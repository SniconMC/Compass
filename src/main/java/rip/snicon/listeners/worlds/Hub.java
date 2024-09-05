package rip.snicon.listeners.worlds;

import com.github.sniconmc.container.creators.HotbarCreator;
import com.github.sniconmc.sidebar.SidebarManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.scoreboard.Sidebar;
import rip.snicon.instances.InstanceCreator;

import java.util.Set;

public class Hub {

    private final EventNode<PlayerEvent> hubNode;

    public Hub(EventNode<Event> node) {
        this.hubNode = EventNode.value("hub", EventFilter.PLAYER, player -> player.getInstance() == InstanceCreator.getInstanceMap().get("hub"));
        onPlayerJoin();
        onPlayerQuit();
        eventsToBeCanceled();
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


    public void eventsToBeCanceled() {
        hubNode.addListener(ItemDropEvent.class, event -> {
            event.setCancelled(true);
        });
    }

}
