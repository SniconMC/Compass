package rip.snicon.compass.inventory.item.items.containers.settings;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.settings.PlayerSetting;

import java.util.List;

public class ShowIconItem extends MysteryItem {
    public ShowIconItem() {
        super(Material.OAK_SIGN, "Show Profession Icon", List.of(), 1, MysteryItemOrigin.CONTAINER);
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        boolean showIcon = player.getSettingsHandler().getSetting(PlayerSetting.SHOW_ICON);
        String desc = PlayerSetting.SHOW_ICON.getDescription();

        // Create status message based on current setting
        String status = showIcon
                ? "<green>Currently: <white>Showing icons</white></green>"
                : "<green>Currently: <white>Showing text</white></green>";

        // Create toggle message
        String toggleHint = showIcon
                ? "<gray>Click to switch to text display</gray>"
                : "<gray>Click to switch to icon display</gray>";

        this.setLore(List.of(
                desc,
                "",
                status,
                toggleHint
        ));
    }

    @Override
    public void onUse(MysteryPlayer player) {
        player.getSettingsHandler().updateSetting(PlayerSetting.SHOW_ICON, !player.getSettingsHandler().getSetting(PlayerSetting.SHOW_ICON));
        populateForPlayer(player);
        if (getHostInventory() != null) {
            this.getHostInventory().setItemStack(getHostSlot(), createItemStack());
        }
    }
}
