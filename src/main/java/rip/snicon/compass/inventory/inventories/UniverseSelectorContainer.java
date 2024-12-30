package rip.snicon.compass.inventory.inventories;

import rip.snicon.compass.inventory.MysteryInventory;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

public class UniverseSelectorContainer extends MysteryInventory {

    public UniverseSelectorContainer() {
        super("Universe Selector");
    }

    @Override
    protected void initialize() {
        // Add static items to the inventory
        setItem(11, MysteryItemType.SHOWICON_ITEM);
        setItem(13, MysteryItemType.SHOWDECIMAL_ITEM);

    }



    @Override
    protected void populate(MysteryPlayer player) {

    }
}