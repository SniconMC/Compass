package rip.snicon.compass.inventory.item.items.containers;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.MysteryInventoryType;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class ProfileViewer extends MysteryItem {

    public ProfileViewer() {
        super(
                Material.PLAYER_HEAD,
                "<gold>Profile</gold>",
                List.of("View your profile"),
                1, 1,
                MysteryItemOrigin.CONTAINER
        );
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        setSkin(player.getSkin());
    }

    @Override
    public void onUse(MysteryPlayer player) {
        player.openInventory(MysteryInventoryType.PROFILE_CONTAINER.getInventory(player).toMinestomInventory(player, InventoryType.CHEST_5_ROW, MysteryItemType.BACKGROUND_ITEM.getItem(player)));
    }
}
