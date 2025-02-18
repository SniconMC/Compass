package rip.snicon.compass.inventory.store;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.PlayerRank;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class BuyRanksMenu extends TemplateInventory {
    public BuyRanksMenu() {
        super(Component.text("Ranks"), InventoryType.CHEST_3_ROW);
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());

        int slot = 10;
        for (PlayerRank rank : PlayerRank.values()) {
            setItem(slot, new TemplateItem(Material.GOLD_INGOT) {
                @Override
                protected void initialize() {
                    setName(TextUtils.convertStringToComponent("<" + rank.getColor() + ">" + TextUtils.capitalizeFirstLetter(rank.name()) + "</" + rank.getColor() + ">"));
                    setLore(TextUtils.convertStringToComponent(List.of("","<gray>This price and rank is temperary</gray>","", "<yellow>Cost: 1000</yellow>")));
                }

                @Override
                protected void personalize(Player player) {

                }

                @Override
                public void onUse(TemplateInventoryEvent templateInventoryEvent) {
                    MysteryPlayer player = (MysteryPlayer) templateInventoryEvent.getPlayer();
                    double emeralds = player.getDataHandler().getEmeralds();
                    PlayerRank currentRank = player.getDataHandler().getRank();

                    // Check if the player already has this rank or a higher one
                    if (currentRank.ordinal() >= rank.ordinal()) {
                        // Just switch to this rank without charging
                        player.getDataHandler().updateRank(rank, true);
                        player.sendMessage(TextUtils.convertStringToComponent("<yellow>Switched to rank: " + rank.name() + "</yellow>"));
                        return;
                    }

                    // If it's a higher rank, allow the purchase
                    if (emeralds >= 200) {
                        player.getDataHandler().updateEmeralds(-200, false);
                        player.getDataHandler().updateRank(rank, true);
                        player.sendMessage(TextUtils.convertStringToComponent("<green>You have upgraded to " + rank.name() + "!</green>"));
                    } else {
                        player.sendMessage(TextUtils.convertStringToComponent("<red>You don't have enough emeralds for this rank.</red>"));
                    }
                }


                @Override
                public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

                }
            });
            slot = slot + 1;
        }
    }

    @Override
    protected void personalize(Player player) {

    }
}
