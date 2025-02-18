package rip.snicon.compass.inventory.store;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class StoreMenu extends TemplateInventory {
    public StoreMenu() {
        super(TextUtils.convertStringToComponent("Shop"), InventoryType.CHEST_4_ROW);
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());

    }

    @Override
    protected void personalize(Player player) {

        MysteryPlayer p = (MysteryPlayer) player;

        setItem(13, new TemplateItem(Material.PLAYER_HEAD) {
            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("<green>Ranks</green>"));
            }

            @Override
            protected void personalize(Player player) {
                setLore(TextUtils.convertStringToComponent(List.of(" ","<white>Current Rank:</white> <"+ p.getDataHandler().getRank().getColor() + ">" + p.getDataHandler().getRank().name() + "</" + p.getDataHandler().getRank().getColor() + ">")));
            }

            @Override
            public void onUse(TemplateInventoryEvent templateInventoryEvent) {
                templateInventoryEvent.getPlayer().openInventory(new BuyRanksMenu().constructInventory(templateInventoryEvent.getPlayer()));
            }

            @Override
            public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

            }
        });
    }
}
