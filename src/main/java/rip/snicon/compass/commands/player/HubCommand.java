package rip.snicon.compass.commands.player;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import rip.snicon.compass.instances.InstanceCreator;
import rip.snicon.compass.instances.worlds.WorldInfo;

public class HubCommand extends Command {

    public HubCommand() {

        super("hub", "lobby", "l");

        // Executed if no other executor can be used
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player player)){
                return;
            }
            WorldInfo worldInfo = InstanceCreator.getWorldMap().get("hub");
            player.teleport(worldInfo.getSpawn());

        });



    }
}
