package rip.snicon.listeners.momentum;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.PlayerEvent;
import rip.snicon.Main;
import rip.snicon.modules.momentum.MomentumConfig;
import rip.snicon.modules.momentum.MomentumExecutor;
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

            if (configMap == null) {
                Main.logger.error("ConfigMap is null");
                return;
            }

            Map<String, Map<String, MomentumConfig>> playerMomentumMap = configMap.get(player);

            if (playerMomentumMap == null) {
                Main.logger.error("PlayerMomentumMap is null");
                return;
            }
            for (String type : playerMomentumMap.keySet()) {
                Map<String, MomentumConfig> typeMap = playerMomentumMap.get(type);

                for (String jsonFile : typeMap.keySet()) {
                    MomentumConfig momentumConfig = typeMap.get(jsonFile);

                    MomentumExecutor.isOnMomentumPad(momentumConfig, player, jsonFile);

                }
            }

        });
    }
}
