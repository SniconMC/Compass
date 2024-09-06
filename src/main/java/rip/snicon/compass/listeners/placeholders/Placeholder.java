package rip.snicon.compass.listeners.placeholders;

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
import java.util.Map;

public class Placeholder {

    private static final Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

    private static final File dataProfileFolder = new File("resources/profiles");


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

            // TODO
            GandalfManager.initiateGandalf(player);

            Map<String, String> profileDataJSONData = new LoadGandalf().load(dataProfileFolder);

            String playerProfile = profileDataJSONData.get(player.getUuid().toString());

            GandalfProfile profile = gson.fromJson(playerProfile, GandalfProfile.class);




            PlaceholderManager.setPlaceholderToPlayer(player, "player_name", player.getUsername());


            PlaceholderManager.setPlaceholderToPlayer(player, "hub_explorer_random", HubExplorerEnum.getRandomText());

            Map<String, String> placeholders = new HashMap<>();

            placeholders.put("profession_format_state", profile.getSettings().getProfession_format());

            placeholders.put("player_visibility_item", "lime_dye");
            placeholders.put("player_visibility_state", "<green>Show Players</green>");

            placeholders.put("player_visibility_item_geri", "gray_dye");
            placeholders.put("player_visibility_state_geri", "<red>Hide Geri</red>");

            PlaceholderManager.addPlaceholdersToPlayer(player, placeholders);


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
