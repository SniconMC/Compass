package rip.snicon.motherfucker;

import com.github.sniconmc.container.ContainerManager;
import com.github.sniconmc.container.utils.ReloadContainer;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class Bitch {

    public static void toggleProfession(Player player, Event event) {

        // Cast the event to InventoryPreClickEvent if needed
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.OAK_SIGN) {

            String oldProfessionFormatState = PlaceholderManager.getPlaceholderForPlayer(player, "profession_format_state");

            switch (oldProfessionFormatState) {
                case "Icon" -> PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Text");
                case "Text" -> PlaceholderManager.setPlaceholderToPlayer(player,"profession_format_state", "Icon");
            }

            // update player profile stat

        }

        ReloadContainer.reloadCurrentContainers(player);

        // update container*/
    }

    public static void togglePlayerVisibility(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<red>Hide Players</red>");

        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<green>Show Players</green>");

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
