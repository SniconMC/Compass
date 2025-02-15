package rip.snicon.compass.inventory.blockhunt.items;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import rip.snicon.compass.utils.TextUtils;

public class BlockhuntPlayItem extends TemplateItem {
    public BlockhuntPlayItem() {
        super(Material.FLOWER_POT);
    }

    @Override
    protected void initialize() {
        setName(TextUtils.convertStringToComponent("<gold>Play Blockhunt</gold>"));
    }

    @Override
    protected void personalize(Player player) {

    }

    @Override
    public void onUse(TemplateInventoryEvent templateInventoryEvent) {

    }

    @Override
    public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

    }
}
