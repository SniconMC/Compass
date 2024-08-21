package rip.snicon.commands.sidebar;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.snicon.modules.sidebar.SidebarCreator;

public class SidebarCommand extends Command {

    public SidebarCommand() {
        super("sidebar");

        // Executed if no other executor can be used
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("/sidebar action <name>");
        });

        var action = ArgumentType.Literal("reload");
        var name = ArgumentType.String("sidebar_name");

        addSyntax((sender, context) -> {
            String actionString = context.get(action);

            if (actionString.equalsIgnoreCase("reload")) {
                SidebarCreator.reloadSidebars();
            }

        }, action);

        addSyntax((sender, context) -> {

            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Execute This Command");
                return;
            }
            String actionString = context.get(action);
            String nameString = context.get(name);

            if (actionString.equalsIgnoreCase("set")) {
                SidebarCreator.setSidebar(player, nameString);
            }

        }, action, name);
    }
}
