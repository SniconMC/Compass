// File: SettingsItem.java
package rip.snicon.compass.inventory.item.items.containers.profile;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.MysteryInventoryType;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class SettingsItem extends MysteryItem {

    public SettingsItem() {
        super(Material.REDSTONE, "» <gold>Settings</gold> «", List.of(
                "Change your settings!",
                "",
                "<gray>» <aqua>Click to open</aqua> «</gray>"
        ), 1, MysteryItemOrigin.CONTAINER);

        setShowTooltip(true);
        setDyeColor("");
        setGlint(false);
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        // Add any dynamic player-specific properties here.
    }

    @Override
    public void onUse(MysteryPlayer player) {
        // Logic for when the player uses this item.
        player.openInventory(MysteryInventoryType.SETTINGS_CONTAINER.getInventory(player).toMinestomInventory(player, InventoryType.CHEST_5_ROW, MysteryItemType.BACKGROUND_ITEM.getItem(player)));
    }

    @Override
    public void onDrop(MysteryPlayer player) {

    }
}