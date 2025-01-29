package rip.snicon.compass.inventory.profile.items;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import rip.snicon.compass.inventory.profession.ProfessionContainer;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.profession.PlayerProfession;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class ProfessionItem extends TemplateItem {

    public ProfessionItem() {
        super(Material.TOTEM_OF_UNDYING);

        setName(TextUtils.convertStringToComponent("<gold>Profession</gold>"));
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;
        // Get player's total XP
        double totalXp = p.getDataHandler().getProfessionXp();
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
        setLore(TextUtils.convertStringToComponent(List.of(
                "<yellow>Profession: " + TextUtils.capitalizeFirstLetter(p.getDataHandler().getProfession().name()) + "</yellow>",
                "<yellow>Profession XP: " + (int) totalXp + " / " + (int) cumulativeXp + "</yellow>",
                "<yellow>Road to " + TextUtils.capitalizeFirstLetter(maxProfession.name()) + "</yellow>",
                progressBar + " <yellow>" + (int) (progress * 100) + "%</yellow>"
        )));
    }

    @Override
    public void onUse(TemplateInventoryEvent event) {
        TemplateInventory inventory = new ProfessionContainer();
        inventory.fillInventory(new BackgroundItem());
        event.getPlayer().openInventory(inventory.constructInventory(event.getPlayer()));
    }

    @Override
    public void onDrop(TemplateInventoryEvent event) {

    }


}
