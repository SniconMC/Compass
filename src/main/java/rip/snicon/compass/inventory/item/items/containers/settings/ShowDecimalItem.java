package rip.snicon.compass.inventory.item.items.containers.settings;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.settings.PlayerSetting;

import java.util.List;

public class ShowDecimalItem extends MysteryItem {
    public ShowDecimalItem() {
        super(Material.GOLD_NUGGET, "Display Format Toggle", List.of(), 1, MysteryItemOrigin.CONTAINER);
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        boolean value = player.getSettingsHandler().getSetting(PlayerSetting.DECIMAL_NUMBERS);
        String desc = PlayerSetting.SHOW_ICON.getDescription();

        if (value) {
            setMaterial(Material.GOLD_NUGGET);
        } else {
            setMaterial(Material.IRON_NUGGET);
        }

        // Create status message based on current setting
        String status = value
                ? "<green>Currently: <white>Showing decimal</white></green>"
                : "<green>Currently: <white>Showing procent</white></green>";

        // Create toggle message
        String toggleHint = value
                ? "<gray>Click to switch to procent display</gray>"
                : "<gray>Click to switch to decimal display</gray>";

        this.setLore(List.of(
                desc,
                "",
                status,
                toggleHint
        ));
    }

    @Override
    public void onUse(MysteryPlayer player) {
        player.getSettingsHandler().updateSetting(PlayerSetting.DECIMAL_NUMBERS, !player.getSettingsHandler().getSetting(PlayerSetting.DECIMAL_NUMBERS));
        populateForPlayer(player);
        if (getHostInventory() != null) {
            this.getHostInventory().setItemStack(getHostSlot(), createItemStack());
        }

    }
    @Override
    public void onDrop(MysteryPlayer player) {

    }
}
