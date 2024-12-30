package rip.snicon.compass.inventory.item.items.containers.profile;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class StatisticsItem extends MysteryItem {

    public StatisticsItem() {
        super(Material.PLAYER_HEAD, "» <gold>Statistics</gold> «", List.of(
                "View all your network-wide",
                "statistics in one place!",
                "",
                "<gray>Total XP:</gray> <yellow>$(player_total_xp)",
                "<gray>Emeralds:</gray> <green>$(player_emeralds)",
                "",
                "<gray>» <aqua>Click to open</aqua> «</gray>"
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
        // Logic for when the player uses this item.

    }

    @Override
    public void onDrop(MysteryPlayer player) {

    }
}
