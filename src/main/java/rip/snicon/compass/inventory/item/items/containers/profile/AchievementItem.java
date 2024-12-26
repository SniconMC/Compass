package rip.snicon.compass.inventory.item.items.containers.profile;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.ColorUtils;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class AchievementItem extends MysteryItem {

    public AchievementItem() {
        super(Material.DIAMOND, "» <#ffaa00>Achievements</#ffaa00> «", List.of(
                "",
                "<gray>Unlocked:</gray> <aqua>100<dark_aqua>/</dark_aqua>1000</aqua> <dark_gray>(10%)</dark_gray>",
                "<gray>Points:</gray> <yellow>$(player_achievement_points)<gold>/</gold>1350</yellow> <dark_gray>(13.27%)</dark_gray>",
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
}