package rip.snicon.compass.inventory.item.items.containers.settings;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.TemplateItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.settings.PlayerSetting;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class ShowIconItem extends TemplateItem {
    public ShowIconItem() {
        super(Material.OAK_SIGN);
    }

    @Override
    protected void initialize() {
        setName(TextUtils.convertStringToComponent("Show Profession Icon"));
    }

    @Override
    protected void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;
        boolean showIcon = p.getSettingsHandler().getSetting(PlayerSetting.SHOW_ICON);
        String desc = PlayerSetting.SHOW_ICON.getDescription();

        if (showIcon) {
            setMaterial(Material.OAK_SIGN);
        } else {
            setMaterial(Material.BIRCH_SIGN);
        }

        // Create status message based on current setting
        String status = showIcon
                ? "<green>Currently: <white>Showing icons</white></green>"
                : "<green>Currently: <white>Showing text</white></green>";

        // Create toggle message
        String toggleHint = showIcon
                ? "<gray>Click to switch to text display</gray>"
                : "<gray>Click to switch to icon display</gray>";

        this.setLore(TextUtils.convertStringToComponent(List.of(
                desc,
                "",
                status,
                toggleHint
        )));
    }

    @Override
    public void onUse(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;
        p.getSettingsHandler().updateSetting(PlayerSetting.SHOW_ICON, !p.getSettingsHandler().getSetting(PlayerSetting.SHOW_ICON));
        personalize(player);
        if (getHostInventory() != null) {
            this.getHostInventory().setItemStack(getHostSlot(), constructItemStack(player));
            p.updateDisplayName();
        }
    }

    @Override
    public void onDrop(Player player) {

    }
}
