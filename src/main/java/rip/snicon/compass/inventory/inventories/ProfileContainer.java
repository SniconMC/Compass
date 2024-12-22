package rip.snicon.compass.inventory.inventories;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.MysteryInventory;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class ProfileContainer extends MysteryInventory {

    public ProfileContainer() {
        super("Your Profile");
    }

    @Override
    protected void initialize() {
        // Add static items to the inventory
        setItem(11, MysteryItemType.ACHIEVEMENT_ITEM);
        setItem(13, MysteryItemType.STATISTICS_ITEM);
        setItem(15, MysteryItemType.COSMETICS_ITEM);
        setItem(22, MysteryItemType.SETTINGS_ITEM);
        setItem(40, MysteryItemType.CLOSE_ITEM);
        setItem(20, MysteryItemType.GUIDES_PHONE_ITEM);
    }



    @Override
    protected void populate(MysteryPlayer player) {
        // Dynamic population logic if needed
        setItem(24, MysteryItemType.PROFESSION_ITEM.getItem(player));
    }
}