package rip.snicon.compass.inventory.item.items.containers;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class BackgroundItem extends MysteryItem {

    public BackgroundItem() {
        super(Material.GRAY_STAINED_GLASS_PANE, "", List.of(

        ), 1, MysteryItemOrigin.CONTAINER);

        setShowTooltip(false);
        setDyeColor("");
        setGlint(false);
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        // Add any dynamic player-specific properties here.
    }

    @Override
    public void onUse(MysteryPlayer player) {
    }
}