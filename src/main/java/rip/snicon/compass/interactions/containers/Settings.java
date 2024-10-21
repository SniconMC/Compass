package rip.snicon.compass.interactions.containers;

import com.github.sniconmc.container.utils.ReloadContainer;
import com.github.sniconmc.gandalf.database.DatabasePlayer;
import com.github.sniconmc.gandalf.utils.ProfileUtils;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.github.sniconmc.gandalf.GandalfManager;
import com.github.sniconmc.gandalf.utils.CalculateProfession;
import com.github.sniconmc.gandalf.utils.TabUtils;

import java.util.UUID;


public class Settings {


    public static void toggleProfession(Player player, Event event) {

        // Cast the event to InventoryPreClickEvent if needed
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        DatabasePlayer dataPlayer = GandalfManager.getDataPlayer(player);
        if (dataPlayer == null) {
            return;
        }
        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.OAK_SIGN) {

            String oldProfessionFormatState = dataPlayer.getProfessionFormat();

            switch (oldProfessionFormatState.toLowerCase()) {
                case "icon" -> {
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Text");

                    dataPlayer.setProfessionFormat("text");
                }

                case "text" -> {
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Icon");

                    dataPlayer.setProfessionFormat("icon");
                }
            }
            ProfileUtils.update(dataPlayer);

            for (Player onlinePLayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                TabUtils.setExistingPlayer(player, onlinePLayer);
            }
        }

        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void togglePlayerVisibility(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        DatabasePlayer dataPlayer = GandalfManager.getDataPlayer(player);
        if (dataPlayer == null) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<red>Hide Players</red>");

            dataPlayer.setPlayerVisibility(false);
        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<green>Show Players</green>");

            dataPlayer.setPlayerVisibility(true);
        }

        updateViewerRule(player, dataPlayer);
        ProfileUtils.update(dataPlayer);
        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void toggleGeriVisibility(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        DatabasePlayer dataPlayer = GandalfManager.getDataPlayer(player);
        if (dataPlayer == null) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<red>Hide Geri</red>");

            dataPlayer.setGeriVisibility(false);
        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<green>Show Geri</green>");

            dataPlayer.setGeriVisibility(true);
        }

        updateViewerRule(player, dataPlayer);
        ProfileUtils.update(dataPlayer);
        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void toggleNumberFormat(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        DatabasePlayer dataPlayer = GandalfManager.getDataPlayer(player);
        if (dataPlayer == null) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.IRON_NUGGET) {
            PlaceholderManager.setPlaceholderToPlayer(player, "profession_gui_progression_item", "gold_nugget");
            PlaceholderManager.setPlaceholderToPlayer(player, "profession_gui_progression_state", "<gradient:#f1d807:#f1b107>Percent</gradient>");
            dataPlayer.setProfessionNumberFormat(false);
        }

        if (clickedItem.material() == Material.GOLD_NUGGET) {
            PlaceholderManager.setPlaceholderToPlayer(player, "profession_gui_progression_item", "iron_nugget");
            PlaceholderManager.setPlaceholderToPlayer(player, "profession_gui_progression_state", "<gradient:#bbc3ce:#959aa2>Decimal</gradient>");

            dataPlayer.setProfessionNumberFormat(true);
        }

        ProfileUtils.update(dataPlayer);
        CalculateProfession.updateProfessionGUI(player, dataPlayer);
        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void updateViewerRule(Player player, DatabasePlayer dataPlayer) {
        boolean playerVisibility = dataPlayer.isPlayerVisibility();
        boolean geriVisibility = dataPlayer.isGeriVisibility();

        // Updating the viewer rule based on the combined visibility settings
        player.updateViewerRule(other -> {
            if (!(other instanceof Player otherPlayer)) {
                return true; // Always show non-player entities
            }

            UUID geriUUID = UUID.fromString("c600eeb7-c7da-4bdd-bff1-d26e71001d39");

            if (playerVisibility && geriVisibility) {
                return true; // Show all players
            } else if (playerVisibility && !geriVisibility) {
                return !otherPlayer.getUuid().equals(geriUUID); // Show all except Geri
            } else if (!playerVisibility && geriVisibility) {
                return otherPlayer.getUuid().equals(geriUUID); // Show only Geri
            } else {
                return false; // Hide all players
            }
        });
    }
}
