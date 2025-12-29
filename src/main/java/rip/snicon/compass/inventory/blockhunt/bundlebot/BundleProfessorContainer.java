package rip.snicon.compass.inventory.blockhunt.bundlebot;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;

public class BundleProfessorContainer extends TemplateInventory {
    public BundleProfessorContainer() {
        super(Component.text("Challanges"), InventoryType.CHEST_4_ROW);
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());
        setItem(31, new CloseButton());
    }

    @Override
    protected void personalize(Player player) {

    }
}
