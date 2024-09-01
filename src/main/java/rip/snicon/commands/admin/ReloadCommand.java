package rip.snicon.commands.admin;

import com.github.sniconmc.momentum.MomentumManager;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import rip.snicon.listeners.placeholders.enums.HubExplorerEnum;
import rip.snicon.modules.oblivion.OblivionCreator;
import rip.snicon.modules.sidebar.SidebarCreator;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload");


        // Executed if no other executor can be used
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("/reload action");
        });

        var action = ArgumentType.Enum("action", ReloadActions.class);

        addSyntax((sender, context) -> {

            if (!(sender instanceof Player player)){
                return;
            }

            String actionString = context.get(action).toString();

            switch (actionString.toLowerCase()) {
                case "sidebar" -> {
                    SidebarCreator.reloadSidebars();
                    sender.sendMessage("Reloaded Sidebar");
                }
                case "oblivion" -> {
                    PlaceholderManager.setPlaceholderToPlayer(player, "hub_explorer_random", HubExplorerEnum.getRandomText());
                    OblivionCreator.reloadOblivions();
                    sender.sendMessage("Reloaded Oblivion");
                }
            }
        }, action);
    }
}
