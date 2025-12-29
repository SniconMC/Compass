package rip.snicon.compass.inventory.fisherman;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import org.w3c.dom.Text;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.bundle.MysteryBundle;
import rip.snicon.compass.player.data.bundle.MysteryBundleTypes;
import rip.snicon.compass.utils.TextUtils;

import java.util.Arrays;
import java.util.List;

public class FishMerchantInventory extends TemplateInventory {

    public FishMerchantInventory() {
        // 5-row chest titled “Fish Merchant”
        super(Component.text("Fish Merchant"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        // 1) Fill the entire inventory with your background item (edges + center)
        fillInventory(new BackgroundItem());

        // 2) Place a Close button (if you want it near the bottom center, slot 40)
        setItem(40, new CloseButton());

        // 3) Define the slots where your shop items should actually appear
        int[] shopSlots = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        };

        // 4) Create your shop items. (Inline anonymous TemplateItems)
        // Example: 2 items in this shop
        TemplateItem fishBundleItem = new TemplateItem(Material.TROPICAL_FISH) {
            private final int cost = 100;
            private final MysteryBundle bundle = MysteryBundle.createBundleFromType(MysteryBundleTypes.FISH_BUNDLE,1, false);
            @Override
            protected void initialize() {
            }

            @Override
            protected void personalize(Player player) {
                setName(bundle.getDisplayNameComponent());
                List<Component> lore = bundle.getLoreComponent();
                List<Component> extraLore = TextUtils.convertStringToComponent(List.of("","<white>Cost: <green>" + cost + "</green></white>","","<yellow>Click To Buy</yellow>"));

                lore.addAll(extraLore);
                setLore(lore);
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                MysteryPlayer player = (MysteryPlayer) event.getPlayer();

                double emeralds = player.getDataHandler().getEmeralds();

                if (emeralds >= cost) {
                    player.sendMessage(TextUtils.convertStringToComponent("You bought an <color:" + bundle.getRarity().getColor() + ">" + bundle.getName() + "</color:" + bundle.getRarity().getColor() + "> for <green>" + cost + "</green> emeralds!"));
                    player.getDataHandler().updateEmeralds(-cost,false);
                    player.getBundleHandler().addBundleAmount(MysteryBundleTypes.FISH_BUNDLE,1);
                } else {
                    player.sendMessage(TextUtils.convertStringToComponent("<red>Fucking poor</red>"));
                }
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {
            }
        };


        // 5) Put your shop items in a list
        List<TemplateItem> shopItems = List.of(fishBundleItem);

        // 6) Loop over the shop slots
        //    - If we still have items left, place them
        //    - Otherwise, fill with air
        for (int i = 0; i < shopSlots.length; i++) {
            if (i < shopItems.size()) {
                setItem(shopSlots[i], shopItems.get(i));
            } else {
                // Fill the remaining shop slots with air
                setItem(shopSlots[i], new TemplateItem(Material.AIR) {
                    @Override
                    protected void initialize() {
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
                });
            }
        }
    }

    @Override
    protected void personalize(Player player) {
        // No per-player personalization in this simple example
    }
}
