package rip.snicon.compass.inventory.profile;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;

import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import rip.snicon.compass.inventory.cosmetics.Perks;
import rip.snicon.compass.inventory.profile.cosmetics.ProfilePerks;
import rip.snicon.compass.utils.TextUtils;

public class ProfileCosmetics extends TemplateInventory {
    public ProfileCosmetics() {
        super(TextUtils.convertStringToComponent("Your Cosmetics"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());

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
                templateInventoryEvent.getPlayer().openInventory(new ProfilePerks().constructInventory(templateInventoryEvent.getPlayer()));
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
