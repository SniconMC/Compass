package rip.snicon.compass.motherfucker;

import com.github.sniconmc.container.utils.ReloadContainer;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import rip.snicon.compass.gandalf.GandalfManager;
import rip.snicon.compass.gandalf.config.GandalfProfile;
import rip.snicon.compass.gandalf.utils.TabUtils;


public class Bitch {


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
                    player.sendMessage("nub");
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Text");

                    profile.getSettings().setProfession_format("text");

                    // Iterate through UUIDs and change appearance using the complex rank format
                    for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        TabUtils.setExistingPlayer(player, onlinePlayer);
                    }


                }
                case "text" -> {
                    player.sendMessage("obama");
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

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<red>Hide Players</red>");

            player.updateViewerRule(other -> !(other instanceof Player));

        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<green>Show Players</green>");

            player.updateViewerRule(null);
        }

        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void toggleGeriVisibility(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }


        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<red>Hide Geri</red>");


        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<green>Show Geri</green>");

        }

        ReloadContainer.reloadCurrentContainers(player);
    }

}
