package rip.snicon.listeners.placeholders;

import com.github.sniconmc.sidebar.SidebarManager;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import rip.snicon.listeners.placeholders.enums.ColorEnum;
import rip.snicon.listeners.placeholders.enums.HubExplorerEnum;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Placeholder {

    private final EventNode<Event> placeholderNode;

    public Placeholder(EventNode<Event> parentNode) {
        this.placeholderNode = EventNode.all("placeholder");
        onGUIInteraction();
        onPlayerConfig();
        onPlayerSpawn();
        onPlayerQuit();
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
        });
    }
    public void onPlayerSpawn(){
        placeholderNode.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();

            Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();

            for (Player onlinePlayer : onlinePlayers) {
                PlaceholderManager.setPlaceholderToPlayer(onlinePlayer, "online_server", String.valueOf(onlinePlayers.size()));
            }
            
            // TODO: make un-static
            String playerRank = "<red>Admin</red>";
            PlaceholderManager.setPlaceholderToPlayer(player, "player_rank", playerRank);
            PlaceholderManager.setPlaceholderToPlayer(player, "player_profession_icon", "<dark_gray>[<gray>?</gray>]</dark_gray>");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_profession", "<gray>Nitwit</gray>");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_emeralds", "427.0");
            PlaceholderManager.setPlaceholderToPlayer(player, "online_network", "12");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_item", "minecraft:tnt");
            SidebarManager.reloadSidebars();
        });
    }
    public void onPlayerQuit() {
        placeholderNode.addListener(PlayerDisconnectEvent.class, event -> {
            Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();

            for (Player onlinePlayer : onlinePlayers) {
                PlaceholderManager.setPlaceholderToPlayer(onlinePlayer, "online_server", String.valueOf(onlinePlayers.size()));
            }
            SidebarManager.reloadSidebars();
        });
    }

}
