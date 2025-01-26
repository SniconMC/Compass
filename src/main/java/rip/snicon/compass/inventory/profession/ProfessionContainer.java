package rip.snicon.compass.inventory.profession;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.TemplateInventory;
import nub.wi1helm.template.TemplateInventoryEvent;
import nub.wi1helm.template.TemplateItem;
import nub.wi1helm.template.items.CloseButton;
import rip.snicon.compass.inventory.profile.items.ProfessionItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.profession.PlayerProfession;
import rip.snicon.compass.player.settings.PlayerSetting;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class ProfessionContainer extends TemplateInventory {

    public ProfessionContainer() {
        super(TextUtils.convertStringToComponent("Profession Levels"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        setItem(40, new CloseButton());
        // No static items here; items will be added dynamically in populate()
    }

    @Override
    protected void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;
        setItem(4, new ProfessionItem());
        double totalXp = p.getDataHandler().getProfessionXp();
        PlayerProfession[] professions = PlayerProfession.values();
        double cumulativeXp = 0;

        int[] slots = getProfessionSlots(); // Slots for professions

        // Get the setting for decimal or percentage display
        boolean useDecimals = p.getSettingsHandler().getSetting(PlayerSetting.DECIMAL_NUMBERS);

        for (int i = 0; i < professions.length; i++) {
            PlayerProfession profession = professions[i];
            double professionXp = profession.getReqXP();

            // Determine if the profession is unlocked
            boolean unlocked = totalXp >= (cumulativeXp + professionXp);

            // XP gained specifically within the current profession
            double xpGained = Math.max(0, totalXp - cumulativeXp);
            double xpRequired = professionXp;

            // Add profession item to inventory
            addProfessionItem(slots[i], profession.name(), unlocked, xpGained, xpRequired, useDecimals);

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
    private void addProfessionItem(int slot, String professionName, boolean unlocked, double xpGained, double xpRequired, boolean useDecimals) {
        Material material = unlocked ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
        String name = unlocked
                ? "<green>Profession: " + TextUtils.capitalizeFirstLetter(professionName) + "</green>"
                : "<red>Profession: " + TextUtils.capitalizeFirstLetter(professionName) + "</red>";

        // Calculate the progress
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

        // Calculate overflow XP
        double overflow = Math.max(0, xpGained - xpRequired);

        // Determine the progress display with conditional overflow
        String progressDisplay = useDecimals
                ? String.format(" <yellow>%.0f / %.0f</yellow>%s",
                xpGained,
                xpRequired,
                overflow > 0 ? String.format(" <gray>(+%.0f)</gray>", overflow) : "")
                : String.format(" <yellow>%.0f%%</yellow>%s",
                progress * 100,
                overflow > 0 ? String.format(" <gray>(+%.0f)</gray>", overflow) : "");

        // Add progress display to the bar
        String progressBarWithDisplay = progressBar + progressDisplay;

        // Add lore, including the progress bar and additional text
        List<String> lore = List.of(
                progressBarWithDisplay, // Progress bar with decimal or percentage
                "<yellow>Click for Detailed View</yellow>", // Instruction text
                unlocked ? "<green>Unlocked</green>" : "<red>Locked</red>" // Locked/unlocked status
        );

        TemplateItem professionItem = new TemplateItem(material) {

            @Override
            protected void initialize() {

            }

            @Override
            protected void personalize(Player player) {

            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                event.getPlayer().sendMessage("You selected: " + professionName);
            }

            @Override
            public void onDrop(TemplateInventoryEvent player) {

            }
        };
        professionItem.setName(TextUtils.convertStringToComponent(name));
        professionItem.setLore(TextUtils.convertStringToComponent(lore));
        setItem(slot, professionItem);
    }







}
