package rip.snicon.compass.inventory.item.items.containers.profile;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.color.Color;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import rip.snicon.compass.inventory.TemplateItem;
import rip.snicon.compass.utils.TextUtils;

import java.awt.*;
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
    public void onUse(Player player) {
        // Logic for when the player uses this item.
    }

    @Override
    public void onDrop(Player player) {
        // Logic for when the player drops this item.
    }
}
