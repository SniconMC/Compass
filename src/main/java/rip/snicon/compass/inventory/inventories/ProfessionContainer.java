package rip.snicon.compass.inventory.inventories;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.MysteryInventory;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.profession.PlayerProfession;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class ProfessionContainer extends MysteryInventory {

    public ProfessionContainer() {
        super("Profession Levels");
    }

    @Override
    protected void initialize() {
        setItem(40, MysteryItemType.CLOSE_ITEM);
        // No static items here; items will be added dynamically in populate()
    }

    @Override
    protected void populate(MysteryPlayer player) {
        setItem(4, MysteryItemType.PROFESSION_ITEM.getItem(player));
        double totalXp = player.getProfessionXp();
        PlayerProfession[] professions = PlayerProfession.values();
        double cumulativeXp = 0;

        int[] slots = getProfessionSlots(); // Slots for professions

        for (int i = 0; i < professions.length; i++) {
            PlayerProfession profession = professions[i];
            double professionXp = profession.getReqXP();

            // Determine if the profession is unlocked
            boolean unlocked = totalXp >= (cumulativeXp + professionXp);

            // XP gained specifically within the current profession
            double xpGained = Math.max(0, totalXp - cumulativeXp);
            double xpRequired = professionXp;

            // Add profession item to inventory
            addProfessionItem(slots[i], profession.name(), unlocked, xpGained, xpRequired);

            // Increment cumulative XP for the next profession
            cumulativeXp += professionXp;
        }
    }


    private int[] getProfessionSlots() {
        // Slots for professions in Row 2 (10–17) and Row 4 (30–38)
        return new int[]{
                10, 11, 12, 13, 14, 15, 16, // Row 2
                28, 29, 30, 31, 32, 33, 34, // Row 4
        };
    }

    /**
     * Adds a profession item to the inventory with the specified color.
     */
    private void addProfessionItem(int slot, String professionName, boolean unlocked, double xpGained, double xpRequired) {
        Material material = unlocked ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
        String name = unlocked
                ? "<green>Profession: " + TextUtils.capitalizeFirstLetter(professionName) + "</green>"
                : "<red>Profession: " + TextUtils.capitalizeFirstLetter(professionName) + "</red>";

        // Calculate the progress percentage
        double progress = Math.min(xpGained / xpRequired, 1.0);
        int totalBars = 20; // Number of dashes in the progress bar
        int filledBars = (int) Math.round(progress * totalBars);

        // Generate the progress bar
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                if (unlocked) {
                    progressBar.append("<green><st> </st></green>");
                } else {
                    progressBar.append("<blue><st> </st></blue>");
                }
            } else {
                progressBar.append("<gray><st> </st></gray>");
            }
        }

        // Add percentage next to the bar
        String progressBarWithPercentage = progressBar + " <yellow>" + (int) (progress * 100) + "%</yellow>";

        // Add lore, including the progress bar and additional text
        List<String> lore = List.of(
                progressBarWithPercentage, // Progress bar with percentage
                "<yellow>Click for Detailed View</yellow>", // Instruction text
                unlocked ? "<green>Unlocked</green>" : "<red>Locked</red>" // Locked/unlocked status
        );

        MysteryItem professionItem = new MysteryItem(material, MysteryItemOrigin.CONTAINER) {

            @Override
            public void populateForPlayer(MysteryPlayer player) {
                // Optional dynamic population logic
            }

            @Override
            public void onUse(MysteryPlayer player) {
                player.sendMessage("You selected: " + professionName);
            }
        };
        professionItem.setName(name);
        professionItem.setLore(lore);
        professionItem.setItemIdentifier(professionName + "_ITEM");
        setItem(slot, professionItem);
    }



}
