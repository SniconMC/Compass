package rip.snicon.compass.inventory.inventories;

import rip.snicon.compass.inventory.MysteryInventory;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

public class SettingsContainer extends MysteryInventory {

    public SettingsContainer() {
        super("Settings");
    }

    @Override
    protected void initialize() {
        // Add static items to the inventory
        setItem(11, MysteryItemType.SHOWICON_ITEM);
        setItem(13, MysteryItemType.STATISTICS_ITEM);

    }



    @Override
    protected void populate(MysteryPlayer player) {

    }
}