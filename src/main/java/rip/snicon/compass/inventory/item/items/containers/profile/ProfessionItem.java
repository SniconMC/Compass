package rip.snicon.compass.inventory.item.items.containers.profile;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.MysteryInventoryType;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.profession.PlayerProfession;
import rip.snicon.compass.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ProfessionItem extends MysteryItem {

    public ProfessionItem() {
        super(Material.TOTEM_OF_UNDYING, "<gold>Profession</gold>", new ArrayList<>(), 1, MysteryItemOrigin.CONTAINER);

        setShowTooltip(true);
        setGlint(false);
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        // Get player's total XP
        double totalXp = player.getDataHandler().getProfessionXp();
        PlayerProfession[] professions = PlayerProfession.values();

        // Calculate the cumulative XP required for the last profession
        double cumulativeXp = 0;
        for (PlayerProfession profession : professions) {
            cumulativeXp += profession.getReqXP();
        }

        // Get the name of the last profession (max profession)
        PlayerProfession maxProfession = professions[professions.length - 1];

        // Calculate progress towards the final profession
        double progress = Math.min(1.0, totalXp / cumulativeXp);

        // Generate the progress bar
        int totalBars = 20; // Number of segments in the progress bar
        int filledBars = (int) Math.round(progress * totalBars);
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                progressBar.append("<green><st> </st></green>"); // Filled segments
            } else {
                progressBar.append("<gray><st> </st></gray>"); // Empty segments
            }
        }

        // Set the lore for the item
        setLore(List.of(
                "<yellow>Profession: " + TextUtils.capitalizeFirstLetter(player.getDataHandler().getProfession().name()) + "</yellow>",
                "<yellow>Profession XP: " + (int) totalXp + " / " + (int) cumulativeXp + "</yellow>",
                "<yellow>Road to " + TextUtils.capitalizeFirstLetter(maxProfession.name()) + "</yellow>",
                progressBar + " <yellow>" + (int) (progress * 100) + "%</yellow>"
        ));
    }



    @Override
    public void onUse(MysteryPlayer player) {
        player.openInventory(MysteryInventoryType.PROFESSION_CONTAINER.getInventory(player).toMinestomInventory(player, InventoryType.CHEST_5_ROW, MysteryItemType.BACKGROUND_ITEM.getItem(player)));
    }
}
