package rip.snicon.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.item.Material;
import rip.snicon.modules.container.ContainerCreator;
import rip.snicon.modules.container.HotbarCreator;
import rip.snicon.modules.oblivion.OblivionCreator;
import rip.snicon.modules.sidebar.SidebarCreator;

import javax.naming.Context;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload");

        // Executed if no other executor can be used
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("/reload action");
        });

        var action = ArgumentType.Enum("action", ReloadActions.class);

        addSyntax((sender, context) -> {
            String actionString = context.get(action).toString();

            switch (actionString.toLowerCase()) {
                case "sidebar":
                    SidebarCreator.reloadSidebars();
                    sender.sendMessage("Realoded Sidebars");
                    break;
                case "oblivion":
                    OblivionCreator.reloadOblivions();
                    sender.sendMessage("Realoded Oblivions");
                    break;
                case "container":
                    ContainerCreator.reloadContainers();
                    sender.sendMessage("Realoded Containers");
                    break;
                case "hotbar":
                    HotbarCreator.reloadHotbars();
                    sender.sendMessage("Realoded Hotbars");
                    break;
            }
        }, action);
    }
}
