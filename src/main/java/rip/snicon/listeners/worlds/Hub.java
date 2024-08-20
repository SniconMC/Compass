package rip.snicon.listeners.worlds;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import rip.snicon.instances.InstanceCreator;

import java.util.Set;

public class Hub {

    private final EventNode<PlayerEvent> hubNode;

    public Hub(EventNode<Event> node) {
        this.hubNode = EventNode.value("hub", EventFilter.PLAYER, player -> player.getInstance() == InstanceCreator.getInstanceMap().get("hub"));
        node.addChild(hubNode);
        onPlayerJoin();

    }

    public void onPlayerJoin() {
        hubNode.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            Instance playerInstance = player.getInstance();

            Set<Player> players = playerInstance.getPlayers();
            for (Player onlinePlayer : players) {
                onlinePlayer.sendMessage("[+] " + player.getUsername());
            }
        });
    }
}
