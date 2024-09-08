package rip.snicon.compass.listeners;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.Instance;
import rip.snicon.compass.instances.CustomInstanceContainer;
import rip.snicon.compass.instances.InstanceCreator;
import rip.snicon.compass.instances.worlds.WorldInfo;

public class VoidListener {

    public void onVoidLimit(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Instance instance = player.getInstance();
        if (instance instanceof CustomInstanceContainer customInstance) {
            // Cast the instance to CustomInstanceContainer

            WorldInfo info = customInstance.getWorldInfo();
            if (info == null) {
                player.sendMessage("World does not exist");
                return;
            }

            double limit = info.getVoidLimitHeight();

            if (player.getPosition().y() <= limit) {
                player.teleport(info.getSpawn());
            }
        }
    }

}
