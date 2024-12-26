package rip.snicon.compass.player;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.instances.regions.MysteryRegion;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemTags;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.profession.PlayerProfession;
import rip.snicon.compass.player.settings.PlayerSetting;
import rip.snicon.compass.utils.TextUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MysteryPlayer extends Player {

    private MysteryRegion region;

    public MysteryPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

    public MysteryPlayer(@NotNull Player player) {
        this(player.getUuid(), player.getUsername(), player.getPlayerConnection());
    }

    public MysteryDataHandler getDataHandler() {
        return MysteryDataHandler.getUser(this.getUuid());
    }

    public void setRank(PlayerRank rank) {
        getDataHandler().updateRank(rank);
    }

    public PlayerRank getRank() {
        return getDataHandler().getRank();
    }

    public void setProfession(PlayerProfession profession) {
        getDataHandler().updateProfession(profession);
    }

    public PlayerProfession getProfession() {
        return getDataHandler().getProfession();
    }

    public void addEmeralds(double amount) {
        double newAmount = getDataHandler().getEmeralds() + amount;
        getDataHandler().updateEmeralds(newAmount);
        sendMessage(TextUtils.convertStringToComponent("You gained " + amount + " emeralds."));
    }

    public void removeEmeralds(double amount) {
        double newAmount = Math.max(0, getDataHandler().getEmeralds() - amount);
        getDataHandler().updateEmeralds(newAmount);
        sendMessage(TextUtils.convertStringToComponent("You lost " + amount + " emeralds."));
    }

    public double getEmeralds() {
        return getDataHandler().getEmeralds();
    }

    public void addProfessionXp(double amount) {
        double newXp = getDataHandler().getProfessionXp() + amount;
        getDataHandler().updateProfessionXp(newXp);
        sendMessage(TextUtils.convertStringToComponent("You gained " + amount + " Profession XP."));
        checkForProfessionLevelUp();
    }

    public double getProfessionXp() {
        return getDataHandler().getProfessionXp();
    }

    public void addAchievementPoints(double points) {
        double newPoints = getDataHandler().getAchievementPoints() + points;
        getDataHandler().updateAchievementPoints(newPoints);
        sendMessage(TextUtils.convertStringToComponent("You gained " + points + " achievement points."));
    }

    public double getAchievementPoints() {
        return getDataHandler().getAchievementPoints();
    }

    private void checkForProfessionLevelUp() {
        double totalXp = getProfessionXp();
        PlayerProfession currentProfession = getProfession();

        PlayerProfession[] professions = PlayerProfession.values();

        int currentIndex = -1;
        for (int i = 0; i < professions.length; i++) {
            if (professions[i] == currentProfession) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) {
            return; // Invalid current profession
        }

        StringBuilder levelUpMessage = new StringBuilder();
        boolean leveledUp = false;
        PlayerProfession leveledProfession = null;
        double cumulativeXp = 0;

        for (int i = 0; i < professions.length; i++) {
            if (i <= currentIndex) {
                cumulativeXp += professions[i].getReqXP();
            } else {
                cumulativeXp += professions[i].getReqXP();
                if (totalXp >= cumulativeXp) {
                    setProfession(professions[i]);
                    leveledUp = true;
                    leveledProfession = professions[i];
                } else {
                    break;
                }
            }
        }

        if (leveledUp) {
            sendMessage(levelUpMessage.append("Congratulations! You leveled up from ")
                    .append(currentProfession.name())
                    .append(" to ")
                    .append(leveledProfession.name()).toString());
        }
    }

    public String getProfessionDisplay() {
        MysteryDataHandler dataHandler = MysteryDataHandler.getUser(this.getUuid());
        boolean showIcon = dataHandler.getSetting(PlayerSetting.SHOW_ICON);

        if (showIcon) {
            return this.getProfession().getIcon().icon(); // Show icon
        } else {
            return TextUtils.capitalizeFirstLetter(this.getProfession().name()); // Show name
        }
    }

    public String getFullDisplayName() {
        String icon = getProfessionDisplay();
        String rankName = getRank().name();
        String rankColor = getRank().getColor();

        // MiniMessage format
        return String.format(
                "<dark_gray>[<gray>%s</gray>] [<%s>%s</%s>]</dark_gray> <white>%s</white>",
                icon, rankColor, TextUtils.capitalizeFirstLetter(rankName), rankColor, getUsername()
        );
    }

    public void updateRegion() {
        for (MysteryRegion newRegion : MysteryRegion.values()) {
            if (newRegion.isInside(getPosition())) {
                if (region != newRegion) {
                    region = newRegion;

                    // Handle region discovery
                    if (!getDataHandler().hasDiscoveredRegion(newRegion)) {
                        getDataHandler().updateDiscoveredRegion(newRegion);

                        String message = String.format(
                                "You discovered: %s and gained %.1f emeralds and %.1f XP!",
                                newRegion.getDisplayName(), newRegion.getEmeralds(), newRegion.getXp()
                        );

                        sendMessage(TextUtils.convertStringToComponent(message));


                    } else {
                        sendMessage(TextUtils.convertStringToComponent("You entered: " + newRegion.getDisplayName()));
                    }
                } else {
                    // Update text for undiscovered neighbors
                    for (MysteryRegion neighbor : newRegion.getBorderingRegions()) {
                        if (!getDataHandler().hasDiscoveredRegion(neighbor)) {
                            neighbor.updateRegionText(this);
                        }
                    }
                    region.debug(this);
                }

                break;
            }
        }
    }

    public List<MysteryRegion> getNeighboringRegions() {
        if (region == null) {
            return Collections.emptyList();
        }
        return region.getBorderingRegions();
    }


    public boolean isInRegion(MysteryRegion newRegion) {
        return region == newRegion;
    }


    public void unloadPlayerInventory() {
        PlayerInventory inventory = this.getInventory();

        for (int slot = 0; slot < inventory.getInnerSize(); slot++) {
            String itemType = inventory.getItemStack(slot).getTag(Tag.String(MysteryItemTags.ITEM_IDENTIFIER.name()));

            if (itemType != null) {
                MysteryItemType item = MysteryItemType.valueOf(itemType);
                getDataHandler().setInventoryItem(slot, item);
            } else {
                getDataHandler().removeInventoryItem(slot);
            }
        }
    }

    public void loadPlayerInventory() {
        // Iterate through the saved inventory map
        for (Map.Entry<Integer, MysteryItemType> entry : this.getDataHandler().getFullInventory().entrySet()) {
            int slot = entry.getKey();
            MysteryItemType itemType = entry.getValue();

            if (itemType != null) {
                // Create the new ItemStack from the MysteryItemType
                ItemStack newItemStack = itemType.getItem(this).createItemStack();

                // Get the existing ItemStack in the slot
                ItemStack existingItemStack = this.getInventory().getItemStack(slot);

                // Update the slot if the item is different or if the slot is empty
                if (!existingItemStack.equals(newItemStack)) {
                    this.getInventory().setItemStack(slot, newItemStack);
                }
            }
        }
    }

    public void addItem(MysteryItemType item) {
        // Find the first available slot
        for (int slot = 0; slot < this.getInventory().getInnerSize(); slot++) {
            if (this.getInventory().getItemStack(slot) == ItemStack.AIR) {
                // Slot is available, add the item
                this.getInventory().setItemStack(slot, item.getItem(this).createItemStack());
                return; // Item added successfully
            }
        }
        // If no slot is available, return false
    }

}

