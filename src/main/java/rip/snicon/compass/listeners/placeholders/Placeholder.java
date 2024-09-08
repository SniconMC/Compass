package rip.snicon.compass.listeners.placeholders;

import com.github.sniconmc.container.config.ContainerItem;
import com.github.sniconmc.container.config.ContainerItemData;
import com.github.sniconmc.container.config.ContainerItemDisplay;
import com.github.sniconmc.container.creators.ContainerCreator;
import com.github.sniconmc.sidebar.SidebarManager;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.google.gson.Gson;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import rip.snicon.compass.gandalf.GandalfManager;
import rip.snicon.compass.gandalf.config.GandalfProfile;
import rip.snicon.compass.gandalf.utils.LoadGandalf;
import rip.snicon.compass.listeners.placeholders.enums.ColorEnum;
import rip.snicon.compass.listeners.placeholders.enums.HubExplorerEnum;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

            GandalfManager.initiateGandalf(player);


            PlaceholderManager.setPlaceholderToPlayer(player, "player_name", player.getUsername());


            PlaceholderManager.setPlaceholderToPlayer(player, "hub_explorer_random", HubExplorerEnum.getRandomText());

            ContainerItem item = new ContainerItem(19   , "green_stained_glass_pane", 1);
            ContainerItemDisplay display = new ContainerItemDisplay(List.of("$(player_profession_icon) $(player_profession_sidebar)"), List.of(List.of("")), false, "", true);
            item.setDisplay(display);
            String json = new Gson().toJson(item, ContainerItem.class);

            ContainerItem item_1 = new ContainerItem(20, "red_stained_glass_pane", 1);
            ContainerItemDisplay display_1 = new ContainerItemDisplay(List.of("Obama"), List.of(List.of("")), false, "", true);
            ContainerItemData data = new ContainerItemData("","profession_detailed_view",false);
            item_1.setDisplay(display_1);
            item_1.setData(data);
            String json_1 = new Gson().toJson(item_1, ContainerItem.class);

            PlaceholderManager.setPlaceholderToPlayer(player, "cool_item", json + "," + json_1);
        });
    }
    public void onPlayerSpawn(){
        placeholderNode.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();

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
