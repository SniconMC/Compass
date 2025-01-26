package rip.snicon.compass.inventory.settings.items;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import nub.wi1helm.template.TemplateInventoryEvent;
import nub.wi1helm.template.TemplateItem;
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
    public void onUse(TemplateInventoryEvent event) {
        final MysteryPlayer player = (MysteryPlayer) event.getPlayer();
        player.getSettingsHandler().updateSetting(PlayerSetting.DECIMAL_NUMBERS, !player.getSettingsHandler().getSetting(PlayerSetting.DECIMAL_NUMBERS));
        personalize(player);
        if (getHostInventory() != null) {
            this.getHostInventory().setItemStack(getHostSlot(), constructItemStack(player));
        }
    }

    @Override
    public void onDrop(TemplateInventoryEvent event) {

    }
}
