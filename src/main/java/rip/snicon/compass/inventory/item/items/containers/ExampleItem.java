package rip.snicon.compass.inventory.item.items.containers;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class ExampleItem extends MysteryItem {

    public ExampleItem() {
        super(Material.DANDELION, "Example Dandelion", List.of(

        ), 1, 64, MysteryItemOrigin.PLAYER);

        setShowTooltip(true);
        setDyeColor("");
        setGlint(true);
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        // Add any dynamic player-specific properties here.
    }

    @Override
    public void onUse(MysteryPlayer player) {
    }
}