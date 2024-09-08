package rip.snicon.compass.commands.admin.world;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.permission.Permission;
import rip.snicon.compass.instances.CustomInstanceContainer;
import rip.snicon.compass.instances.InstanceCreator;
import rip.snicon.compass.instances.worlds.WorldInfo;

public class TravelCommand extends Command {

    public TravelCommand() {
        super("travel", "travel");
        setCondition((sender, commandString) -> sender.hasPermission(new Permission("command.admin.travel")));
        // Executed if no other executor can be used
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("/travel <world name>");
        });

        var worldArgument = ArgumentType.String("world");

        addSyntax((commandSender, commandContext) -> {
            String worldName = commandContext.get(worldArgument);

            if (!(commandSender instanceof Player player)) {
                commandSender.sendMessage("Sender not player");
                return;
            }

            Instance instance = InstanceCreator.getInstance().getInstanceByWorldName(worldName);
            // Check if the instance is of type CustomInstanceContainer
            if (instance instanceof CustomInstanceContainer customInstance) {
                // Cast the instance to CustomInstanceContainer

                WorldInfo info = customInstance.getWorldInfo();
                if (info == null) {
                    commandSender.sendMessage("World does not exist");
                    return;
                }

                Pos instanceStartingPos = new Pos(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ(), info.getSpawnYaw(), info.getSpawnPitch());

                player.setInstance(instance, instanceStartingPos);


            }
        }, worldArgument);
    }
}
