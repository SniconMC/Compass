package rip.snicon.compass.interactions.containers;

import com.github.sniconmc.container.utils.ReloadContainer;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.github.sniconmc.gandalf.GandalfManager;
import com.github.sniconmc.gandalf.config.GandalfProfile;
import com.github.sniconmc.gandalf.utils.CalculateProfession;
import com.github.sniconmc.gandalf.utils.TabUtils;

import java.util.UUID;


public class Settings {


    public static void toggleProfession(Player player, Event event) {

        // Cast the event to InventoryPreClickEvent if needed
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        GandalfProfile profile = GandalfManager.getProfiles(player);
        if (profile == null) {
            return;
        }
        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.OAK_SIGN) {

            String oldProfessionFormatState = profile.getSettings().getProfession_format();

            switch (oldProfessionFormatState.toLowerCase()) {
                case "icon" -> {
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Text");

                    profile.getSettings().setProfession_format("text");

                    // Iterate through UUIDs and change appearance using the complex rank format
                    for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        TabUtils.setExistingPlayer(player, onlinePlayer);
                    }


                }
                case "text" -> {
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Icon");

                    profile.getSettings().setProfession_format("icon");
                    // Iterate through UUIDs and change appearance using the simple rank format
                    for (Player onlinePLayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        TabUtils.setExistingPlayer(player, onlinePLayer);
                    }
                }
            }
            GandalfManager.saveProfileToFile(player.getUuid().toString(), profile);
        }

        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void togglePlayerVisibility(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        GandalfProfile profile = GandalfManager.getProfiles(player);
        if (profile == null) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<red>Hide Players</red>");

            profile.getSettings().setPlayer_visibility(false);
        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<green>Show Players</green>");

            profile.getSettings().setPlayer_visibility(true);
        }

        updateViewerRule(player, profile);
        GandalfManager.saveProfileToFile(player.getUuid().toString(), profile);
        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void toggleGeriVisibility(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        GandalfProfile profile = GandalfManager.getProfiles(player);
        if (profile == null) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<red>Hide Geri</red>");

            profile.getSettings().setGeri_visibility(false);
        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<green>Show Geri</green>");

            profile.getSettings().setGeri_visibility(true);
        }

        updateViewerRule(player, profile);
        GandalfManager.saveProfileToFile(player.getUuid().toString(), profile);
        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void toggleNumberFormat(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        GandalfProfile profile = GandalfManager.getProfiles(player);
        if (profile == null) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.IRON_NUGGET) {
            PlaceholderManager.setPlaceholderToPlayer(player, "profession_gui_progression_item", "gold_nugget");
            PlaceholderManager.setPlaceholderToPlayer(player, "profession_gui_progression_state", "<gradient:#f1d807:#f1b107>Percent</gradient>");
            profile.getSettings().setProfession_number_format(false);
        }

        if (clickedItem.material() == Material.GOLD_NUGGET) {
            PlaceholderManager.setPlaceholderToPlayer(player, "profession_gui_progression_item", "iron_nugget");
            PlaceholderManager.setPlaceholderToPlayer(player, "profession_gui_progression_state", "<gradient:#bbc3ce:#959aa2>Decimal</gradient>");

            profile.getSettings().setProfession_number_format(true);
        }

        updateViewerRule(player, profile);
        GandalfManager.saveProfileToFile(player.getUuid().toString(), profile);
        CalculateProfession.updateProfessionGUI(player, profile);
        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void updateViewerRule(Player player, GandalfProfile profile) {
        boolean playerVisibility = profile.getSettings().isPlayer_visibility();
        boolean geriVisibility = profile.getSettings().isGeri_visibility();

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
