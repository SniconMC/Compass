package rip.snicon.listeners.placeholders;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.instances.worlds.WorldInfo;
import rip.snicon.listeners.placeholders.enums.ColorEnum;
import rip.snicon.listeners.placeholders.enums.HubExplorerEnum;
import rip.snicon.modules.placeholders.PlaceholderManager;
import rip.snicon.utils.PlaceholderReplacer;

public class Placeholder {

    private final EventNode<Event> placeholderNode;

    public Placeholder(EventNode<Event> parentNode) {
        this.placeholderNode = EventNode.all("placeholder");
        onGUIInteraction();
        onPlayerConfig();
        parentNode.addChild(placeholderNode);
    }

    private void onGUIInteraction(){

        placeholderNode.addListener(InventoryOpenEvent.class, event -> {
            Player player = event.getPlayer();
            Pos position = player.getPosition();

            int x = position.blockX();
            int y = position.blockY();
            int z = position.blockZ();

            String coords = x + ", " + y + ", " + z;

            PlaceholderManager.setPlaceholderToPlayer(player, "player_coords", coords);
            PlaceholderManager.setPlaceholderToPlayer(player, "cosmetics_dye", ColorEnum.getRandomColorCode());
        });
    }

    public void onPlayerConfig(){
        placeholderNode.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();

            PlaceholderManager.setPlaceholderToPlayer(player, "player_name", player.getUsername());

            PlaceholderManager.setPlaceholderToPlayer(player, "hub_explorer_random", HubExplorerEnum.getRandomText());


            // TODO: make un-static
            String playerRank = "<dark_gray>[<dark_red>Obama++</dark_red>]</dark_gray>";
            PlaceholderManager.setPlaceholderToPlayer(player, "player_rank", playerRank);
            PlaceholderManager.setPlaceholderToPlayer(player, "player_item", "minecraft:tnt");
        });
    }

}
