package rip.snicon.compass.inventory.item.items.containers.profile;


import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class CosmeticsItem extends MysteryItem {

    public CosmeticsItem() {
        super(Material.LEATHER_CHESTPLATE, "» <gold>Cosmetics</gold> «", List.of(
                "Choose between custom outfits,",
                "items, particles, music, and more!",
                "",
                "<gray>Lorem:</gray> <yellow>Ipsum</yellow>",
                "<gray>Dolor:</gray> <green>Sit</green>",
                "",
                "<gray>» <aqua>Click to open</aqua> «</gray>"
        ), 1, 64, MysteryItemOrigin.CONTAINER);

        setShowTooltip(true);
        setDyeColor("#0077c3");
        setGlint(false);
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        // Add any dynamic player-specific properties here.
    }

    @Override
    public void onUse(MysteryPlayer player) {
        // Logic for when the player uses this item.
    }
}
