package rip.snicon.compass.inventory.item.items.containers;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class MinigameSelector extends MysteryItem {

    public MinigameSelector() {
        super(
                Material.COMPASS,
                "<light_purple>Minigame Selector</light_purple>",
                List.of("Select a minigame to play"),
                1,
                MysteryItemOrigin.CONTAINER
        );
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {

    }

    @Override
    public void onUse(MysteryPlayer player) {
        System.out.println("Opening minigame selector GUI...");
    }
}
