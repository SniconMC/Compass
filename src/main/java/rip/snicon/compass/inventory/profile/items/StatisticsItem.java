package rip.snicon.compass.inventory.profile.items;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import nub.wi1helm.template.TemplateInventoryEvent;
import nub.wi1helm.template.TemplateItem;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class StatisticsItem extends TemplateItem {

    public StatisticsItem() {
        super(Material.PLAYER_HEAD);

    }

    @Override
    protected void initialize() {
        setName(TextUtils.convertStringToComponent("» <gold>Statistics</gold> «"));
        setLore(TextUtils.convertStringToComponent(List.of(
                "View all your network-wide",
                "statistics in one place!",
                "",
                "<gray>Total XP:</gray> <yellow>$(player_total_xp)",
                "<gray>Emeralds:</gray> <green>$(player_emeralds)",
                "",
                "<gray>» <aqua>Click to open</aqua> «</gray>"
        )));
    }

    @Override
    protected void personalize(Player player) {

    }

    @Override
    public void onUse(TemplateInventoryEvent event) {

    }

    @Override
    public void onDrop(TemplateInventoryEvent event) {

    }


}
