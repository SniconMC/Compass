package rip.snicon.compass.inventory.inventories;

import rip.snicon.compass.inventory.MysteryInventory;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

public class MinigameSelectorContainer extends MysteryInventory {

    public MinigameSelectorContainer() {
        super("Minigame Selector");
    }

    @Override
    protected void initialize() {
        // Add static items to the inventory
        setItem(11, MysteryItemType.PARKOUR_ITEM);
        setItem(15, MysteryItemType.BLOCKHUNT_ITEM);

    }



    @Override
    protected void populate(MysteryPlayer player) {

    }
}