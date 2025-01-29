package rip.snicon.compass.inventory.bundels;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.bundle.MysteryBundle;
import rip.snicon.compass.player.handler.MysteryBundleHandler;
import rip.snicon.compass.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class BundleViewerContainer extends TemplateInventory {

    public BundleViewerContainer() {
        super(TextUtils.convertStringToComponent("You'r Bundels"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        setItem(40, new CloseButton()); // Close button
    }

    @Override
    protected void personalize(Player player) {

        final MysteryPlayer p = (MysteryPlayer) player;

        MysteryBundleHandler bundleHandler = p.getBundleHandler();
        List<MysteryBundle> bundles = new ArrayList<>(bundleHandler.getAllBundles());

        int[] slots = getBundleSlots(); // Predefined slots for bundles

        for (int i = 0; i < slots.length; i++) {
            if (i < bundles.size()) {
                // Populate with a bundle
                addBundleItem(slots[i], bundles.get(i));
            } else {
                // Set unused slots to air
                setItem(slots[i], new TemplateItem(Material.AIR) {
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

    private int[] getBundleSlots() {
        // Predefined slots for bundles in the inventory
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,  // Row 2
                19, 20, 21, 22, 23, 24, 25,  // Row 3
                28, 29, 30, 31, 32, 33, 34   // Row 4
        };
    }

    private void addBundleItem(int slot, MysteryBundle bundle) {
        // Determine the material for the bundle item
        Material material = Material.fromNamespaceId(bundle.getDisplayItem())   ;

        // Display name with rarity color
        String name = String.format(
                "<gradient:%s:%s>%s</gradient>",
                bundle.getRarity().getColor(),
                bundle.getRarity().getColor(),
                TextUtils.capitalizeFirstLetter(bundle.getName())
        );

        // Lore containing blocks, entities, and rarity
        List<String> lore = List.of(
                "<yellow>Blocks:</yellow> " + summarize(bundle.getMaterials(), 5),
                "<yellow>Entities:</yellow> " + summarize(bundle.getEntities(), 3),
                String.format("<yellow>Rarity:</yellow> <gradient:%s>%s</gradient>",
                        bundle.getRarity().getColor(),
                        TextUtils.capitalizeFirstLetter(bundle.getRarity().name()))
        );

        TemplateItem bundleItem = new TemplateItem(material) {
            @Override
            protected void initialize() {

            }

            @Override
            protected void personalize(Player player) {

            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                event.getPlayer().sendMessage("Viewing details for bundle: " + bundle.getName());
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

            }
        };
        bundleItem.setName(TextUtils.convertStringToComponent(name));
        bundleItem.setLore(TextUtils.convertStringToComponent(lore));
        setItem(slot, bundleItem);
    }

    private String summarize(List<String> items, int max) {
        if (items == null || items.isEmpty()) return "<gray>None</gray>";
        return items.size() > max
                ? String.join(", ", items.subList(0, max)) + "..."
                : String.join(", ", items);
    }
}
