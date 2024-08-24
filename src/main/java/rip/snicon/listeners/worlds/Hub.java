package rip.snicon.listeners.worlds;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerRespawnEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.scoreboard.Sidebar;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.modules.container.HotbarCreator;
import rip.snicon.modules.oblivion.OblivionCreator;
import rip.snicon.modules.sidebar.SidebarCreator;

import java.util.Set;

public class Hub {

    private final EventNode<PlayerEvent> hubNode;

    public Hub(EventNode<Event> node) {
        this.hubNode = EventNode.value("hub", EventFilter.PLAYER, player -> player.getInstance() == InstanceCreator.getInstanceMap().get("hub"));
        node.addChild(hubNode);
        onPlayerJoin();
        eventsToBeCanceled();

    }

    public void onPlayerJoin() {
        hubNode.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            Instance playerInstance = player.getInstance();
            OblivionCreator.spawnOblivions(player);
            Set<Player> players = playerInstance.getPlayers();
            for (Player onlinePlayer : players) {
                SidebarCreator.setSidebar(player, "hub_sidebar");
                HotbarCreator.setHotbar(player, "lobby");

                onlinePlayer.sendMessage("[+] " + player.getUsername());
                onlinePlayer.sendMessage("Entities in the world: " + player.getInstance().getEntities().size());

            }
        });
    }

    public void eventsToBeCanceled() {

        hubNode.addListener(ItemDropEvent.class, event -> {
            event.setCancelled(true);
        });
    }

}
