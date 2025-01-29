package rip.snicon.compass.inventory.profile;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.inventory.profile.items.*;
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