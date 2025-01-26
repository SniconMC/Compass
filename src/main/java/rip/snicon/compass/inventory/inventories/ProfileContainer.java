package rip.snicon.compass.inventory.inventories;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import rip.snicon.compass.inventory.TemplateInventory;
import rip.snicon.compass.inventory.item.items.containers.CloseButton;
import rip.snicon.compass.inventory.item.items.containers.profile.*;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

public class ProfileContainer extends TemplateInventory {

    public ProfileContainer() {
        super(TextUtils.convertStringToComponent("Your Profile"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        // Add static items to the inventory
        setItem(11, new AchievementItem());
        setItem(13, new StatisticsItem());
        setItem(15, new CosmeticsItem());
        setItem(22, new SettingsItem());
        setItem(40, new CloseButton());
        setItem(20, new GuidesPhoneItem());
    }

    @Override
    protected void personalize(Player player) {
        setItem(24, new ProfessionItem());
    }
}