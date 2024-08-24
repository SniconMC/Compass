package rip.snicon.commands.world;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.instances.worlds.WorldInfo;

public class TravelCommand extends Command {

    public TravelCommand() {
        super("travel", "travel");

        // Executed if no other executor can be used
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("/travel <world name>");
        });

        var worldArgument = ArgumentType.String("world");

        addSyntax((commandSender, commandContext) -> {
            String worldName = commandContext.get(worldArgument);

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("Sender not player");
                return;
            }

            Instance instance = InstanceCreator.getInstanceMap().get(worldName);
            WorldInfo info = InstanceCreator.getWorldMap().get(worldName);
            if (instance == null || info == null) {
                commandSender.sendMessage("World does not exist");
                return;
            }

            Pos instanceStartingPos = new Pos(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ(), info.getSpawnYaw(), info.getSpawnPitch());

            Player player = (Player) commandSender;

            player.setInstance(instance, instanceStartingPos);



        }, worldArgument);



    }
}
