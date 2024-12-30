package rip.snicon.compass.inventory.inventories;

import net.minestom.server.entity.Player;
import rip.snicon.compass.inventory.MysteryInventory;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

public class DefaultInventory extends MysteryInventory {

    public DefaultInventory() {
        super("Default Inventory");
    }

    @Override
    protected void initialize() {
        // Define default items for the default inventory
        setItem(3, MysteryItemType.PROFESSION_VIEWER);
        setItem(4, MysteryItemType.MINIGAME_SELECTOR);
        setItem(5, MysteryItemType.PROFILE_VIEWER);
        setItem(8, MysteryItemType.UNIVERSE_SELECTOR);

    }

    @Override
    protected void populate(MysteryPlayer player) {

    }


}
