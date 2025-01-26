package rip.snicon.compass.inventory.item.items.containers.profile;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.TemplateItem;

import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class SettingsItem extends TemplateItem {

    public SettingsItem() {
        super(Material.REDSTONE);

    }

    @Override
    protected void initialize() {
        setName(TextUtils.convertStringToComponent("» <gold>Settings</gold> «"));
        setLore(TextUtils.convertStringToComponent(List.of(
                "Change your settings!",
                "",
                "<gray>» <aqua>Click to open</aqua> «</gray>"
        )));
    }

    @Override
    protected void personalize(Player player) {

    }

    @Override
    public void onUse(Player player) {

    }

    @Override
    public void onDrop(Player player) {

    }
}