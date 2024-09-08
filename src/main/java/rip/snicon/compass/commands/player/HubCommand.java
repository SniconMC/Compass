package rip.snicon.compass.commands.player;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.permission.Permission;
import rip.snicon.compass.instances.CustomInstanceContainer;
import rip.snicon.compass.instances.InstanceCreator;
import rip.snicon.compass.instances.worlds.WorldInfo;

public class HubCommand extends Command {

    public HubCommand() {

        super("hub", "lobby", "l");

        setCondition((sender, commandString) -> sender.hasPermission(new Permission("command.hub")));
        // Executed if no other executor can be used
        setDefaultExecutor((sender, context) -> {

            if (!(sender instanceof Player player)){
                return;
            }


            Instance instance = InstanceCreator.getInstance().getInstanceByWorldName("hub");
            if (instance instanceof CustomInstanceContainer customInstance) {
                // Cast the instance to CustomInstanceContainer

                WorldInfo info = customInstance.getWorldInfo();
                if (info == null) {
                    sender.sendMessage("World does not exist");
                    return;
                }
                if (player.getInstance() == instance) {
                    player.teleport(info.getSpawn());
                } else {
                    player.setInstance(instance, info.getSpawn());
                }
            }
        });



    }
}
