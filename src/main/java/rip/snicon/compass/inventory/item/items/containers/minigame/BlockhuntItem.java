package rip.snicon.compass.inventory.item.items.containers.minigame;

import net.minestom.server.item.Material;
import rip.snicon.compass.ServerRegistry;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class BlockhuntItem extends MysteryItem {

    public BlockhuntItem() {
        super(Material.CRAFTING_TABLE, "<gold>Blockhunt++</gold>", List.of(
                ""
        ), 1, MysteryItemOrigin.CONTAINER);

        setShowTooltip(true);
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

    @Override
    public void onDrop(MysteryPlayer player) {

    }
}