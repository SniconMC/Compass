package rip.snicon.compass.inventory;

import net.minestom.server.entity.Player;
import rip.snicon.compass.inventory.inventories.DefaultInventory;
import rip.snicon.compass.inventory.inventories.ProfessionContainer;
import rip.snicon.compass.inventory.inventories.ProfileContainer;
import rip.snicon.compass.player.MysteryPlayer;

public enum MysteryInventoryType {
    DEFAULT(new DefaultInventory()), // Add Default Inventory
    PROFESSION_CONTAINER(new ProfessionContainer()),
    PROFILE_CONTAINER(new ProfileContainer());
    private final MysteryInventory inventory;

    MysteryInventoryType(MysteryInventory inventory) {
        this.inventory = inventory;
    }

    public MysteryInventory getInventory(MysteryPlayer player) {

        inventory.populate(player);

        return inventory;
    }
    public MysteryInventory getStaticInventory() {

        return inventory;
    }
}
