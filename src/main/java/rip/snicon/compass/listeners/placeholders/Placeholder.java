package rip.snicon.compass.listeners.placeholders;

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
import com.github.sniconmc.gandalf.GandalfManager;
import com.github.sniconmc.gandalf.config.GandalfProfile;
import com.github.sniconmc.gandalf.utils.CalculateProfession;
import rip.snicon.compass.listeners.placeholders.enums.ColorEnum;
import rip.snicon.compass.listeners.placeholders.enums.HubExplorerEnum;

import java.util.Collection;

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

            GandalfManager.initiateGandalf(player);

            GandalfProfile profile = GandalfManager.getProfiles(player);

            PlaceholderManager.setPlaceholderToPlayer(player, "player_name", player.getUsername());
            PlaceholderManager.setPlaceholderToPlayer(player, "hub_explorer_random", HubExplorerEnum.getRandomText());

            CalculateProfession.updateProfessionGUI(player, profile);
        });
    }
    public void onPlayerSpawn(){
        placeholderNode.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();

            GandalfProfile profile = GandalfManager.getProfiles(player);

            CalculateProfession.updateProfession(player);
            CalculateProfession.updateProfessionGUI(player, profile);

            Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();

            for (Player onlinePlayer : onlinePlayers) {
                PlaceholderManager.setPlaceholderToPlayer(onlinePlayer, "online_server", String.valueOf(onlinePlayers.size()));
            }


            PlaceholderManager.setPlaceholderToPlayer(player, "online_network", "12");
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
