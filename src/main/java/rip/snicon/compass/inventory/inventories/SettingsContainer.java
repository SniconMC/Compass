package rip.snicon.compass.inventory.inventories;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import rip.snicon.compass.inventory.TemplateInventory;
import rip.snicon.compass.inventory.item.items.containers.settings.ShowDecimalItem;
import rip.snicon.compass.inventory.item.items.containers.settings.ShowIconItem;
import rip.snicon.compass.utils.TextUtils;

public class SettingsContainer extends TemplateInventory {

    public SettingsContainer() {
        super(TextUtils.convertStringToComponent("Settings"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        // Add static items to the inventory
        setItem(11, new ShowDecimalItem());
        setItem(13, new ShowIconItem());

    }

    @Override
    protected void personalize(Player player) {

    }
}