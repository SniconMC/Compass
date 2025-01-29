package rip.snicon.compass.inventory.cosmetics;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import rip.snicon.compass.utils.TextUtils;

public class CosmeticsShop extends TemplateInventory {
    public CosmeticsShop() {
        super(TextUtils.convertStringToComponent("Cosmetics"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        setItem(25, new TemplateItem(Material.PUFFERFISH_SPAWN_EGG) {
            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("Player Perks"));
            }

            @Override
            protected void personalize(Player player) {

            }

            @Override
            public void onUse(TemplateInventoryEvent templateInventoryEvent) {
                templateInventoryEvent.getPlayer().openInventory(new Perks().constructInventory(templateInventoryEvent.getPlayer()));
            }

            @Override
            public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

            }
        });
    }

    @Override
    protected void personalize(Player player) {

    }
}
