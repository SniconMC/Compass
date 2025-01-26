package rip.snicon.compass.inventory.item.items.containers.settings;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.TemplateItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.settings.PlayerSetting;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class ShowDecimalItem extends TemplateItem {
    public ShowDecimalItem() {
        super(Material.GOLD_NUGGET);
    }

    @Override
    protected void initialize() {
        setName(TextUtils.convertStringToComponent("Display Format Toggle"));
    }

    @Override
    protected void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;
        boolean value = p.getSettingsHandler().getSetting(PlayerSetting.DECIMAL_NUMBERS);
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
        p.getSettingsHandler().updateSetting(PlayerSetting.DECIMAL_NUMBERS, !p.getSettingsHandler().getSetting(PlayerSetting.DECIMAL_NUMBERS));
        personalize(player);
        if (getHostInventory() != null) {
            this.getHostInventory().setItemStack(getHostSlot(), constructItemStack(player));
        }
    }

    @Override
    public void onDrop(Player player) {

    }
}
