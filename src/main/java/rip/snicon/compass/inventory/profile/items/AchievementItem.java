package rip.snicon.compass.inventory.profile.items;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class AchievementItem extends TemplateItem {

    public AchievementItem() {
        super(Material.DIAMOND);
    }

    @Override
    protected void initialize() {
        setName(TextUtils.convertStringToComponent("» <#ffaa00>Achievements</#ffaa00> «"));
        setLore(TextUtils.convertStringToComponent(List.of(
                "",
                "<gray>Unlocked:</gray> <aqua>100<dark_aqua>/</dark_aqua>1000</aqua> <dark_gray>(10%)</dark_gray>",
                "<gray>Points:</gray> <yellow>$(player_achievement_points)<gold>/</gold>1350</yellow> <dark_gray>(13.27%)</dark_gray>",
                "",
                "<gray>» <aqua>Click to open</aqua> «</gray>")));
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