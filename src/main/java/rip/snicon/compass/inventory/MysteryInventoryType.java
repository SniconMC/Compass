package rip.snicon.compass.inventory;

import rip.snicon.compass.inventory.inventories.*;
import rip.snicon.compass.player.MysteryPlayer;

public enum MysteryInventoryType {
    DEFAULT(new DefaultInventory()), // Add Default Inventory
    PROFESSION_CONTAINER(new ProfessionContainer()),
    PROFILE_CONTAINER(new ProfileContainer()),
    SETTINGS_CONTAINER(new SettingsContainer()),
    MINIGAME_SELECTOR(new MinigameSelectorContainer()),
    UNIVERSE_SELECTOR(new UniverseSelectorContainer());
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
