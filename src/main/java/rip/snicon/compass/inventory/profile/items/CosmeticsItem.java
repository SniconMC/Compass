package rip.snicon.compass.inventory.profile.items;

import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.color.Color;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import rip.snicon.compass.inventory.profile.ProfileCosmetics;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;
import java.util.Objects;

public class CosmeticsItem extends TemplateItem {

    public CosmeticsItem() {
        super(Material.LEATHER_CHESTPLATE);
    }

    @Override
    protected void initialize() {
        setName(TextUtils.convertStringToComponent("» <gold>Cosmetics</gold> «"));
        setLore(TextUtils.convertStringToComponent(List.of(
                "Choose between custom outfits,",
                "items, particles, music, and more!",
                "",
                "<gray>Lorem:</gray> <yellow>Ipsum</yellow>",
                "<gray>Dolor:</gray> <green>Sit</green>",
                "",
                "<gray>» <aqua>Click to open</aqua> «</gray>"
        )));
        setDyeColor(DyedItemColor.LEATHER.withColor(Color.fromRGBLike(Objects.requireNonNull(TextColor.fromHexString("#0077c3")))));
    }

    @Override
    protected void personalize(Player player) {
        // Add any dynamic player-specific properties here if needed.
    }

    @Override
    public void onUse(TemplateInventoryEvent event) {
        event.getPlayer().openInventory(new ProfileCosmetics().constructInventory(event.getPlayer()));
    }

    @Override
    public void onDrop(TemplateInventoryEvent event) {

    }


}
