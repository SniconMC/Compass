package rip.snicon.compass.inventory.profile.items;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import rip.snicon.compass.inventory.settings.SettingsContainer;
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
    public void onUse(TemplateInventoryEvent event) {
        TemplateInventory inventory = new SettingsContainer();
        inventory.fillInventory(new BackgroundItem());
        event.getPlayer().openInventory(inventory.constructInventory(event.getPlayer()));

    }

    @Override
    public void onDrop(TemplateInventoryEvent event) {

    }


}