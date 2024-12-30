package rip.snicon.compass.inventory.item.items.containers;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.MysteryInventoryType;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class MinigameSelector extends MysteryItem {

    public MinigameSelector() {
        super(
                Material.COMPASS,
                "<light_purple>Minigame Selector</light_purple>",
                List.of("Select a minigame to play"),
                1,
                MysteryItemOrigin.CONTAINER
        );
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {

    }

    @Override
    public void onUse(MysteryPlayer player) {
        player.openInventory(MysteryInventoryType.MINIGAME_SELECTOR.getInventory(player).toMinestomInventory(player, InventoryType.CHEST_5_ROW, MysteryItemType.BACKGROUND_ITEM.getItem(player)));
    }

    @Override
    public void onDrop(MysteryPlayer player) {

    }
}
