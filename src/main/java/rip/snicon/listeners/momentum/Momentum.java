package rip.snicon.listeners.momentum;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.PlayerEvent;
import rip.snicon.modules.momentum.MomentumConfig;
import rip.snicon.modules.momentum.MomentumManager;

import java.util.Map;

public class Momentum {

    private final EventNode<PlayerEvent>  momentumNode;

    public Momentum(EventNode<Event> parentNode) {
        this.momentumNode = EventNode.type("momentum", EventFilter.PLAYER);
        onPlayerJoin();
        onPlayerMove();
        parentNode.addChild(momentumNode);
    }

    private void onPlayerJoin() {
        momentumNode.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            MomentumManager.createMomentumPads(player);
        });
    }

    private void onPlayerMove(){
        momentumNode.addListener(PlayerMoveEvent.class, event -> {
            Player player = event.getPlayer();

            Map<Player, Map<String, Map<String, MomentumConfig>>> configMap = MomentumManager.getConfigMap();

            Map<String, Map<String, MomentumConfig>> playerMomentumMap = configMap.get(player);



        });
    }
}
