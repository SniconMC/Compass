package rip.snicon.compass.inventory.inventories;

import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.MysteryInventory;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.bundle.MysteryBundle;
import rip.snicon.compass.player.handler.MysteryBundleHandler;
import rip.snicon.compass.utils.MysteryRarities;
import rip.snicon.compass.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class BundleViewerContainer extends MysteryInventory {

    public BundleViewerContainer() {
        super("Your Bundles");
    }

    @Override
    protected void initialize() {
        setItem(40, MysteryItemType.CLOSE_ITEM); // Close button
    }

    @Override
    protected void populate(MysteryPlayer player) {
        MysteryBundleHandler bundleHandler = player.getBundleHandler();
        List<MysteryBundle> bundles = new ArrayList<>(bundleHandler.getAllBundles());

        int[] slots = getBundleSlots(); // Predefined slots for bundles

        for (int i = 0; i < slots.length; i++) {
            if (i < bundles.size()) {
                // Populate with a bundle
                addBundleItem(slots[i], bundles.get(i));
            } else {
                // Set unused slots to air
                setItem(slots[i], new MysteryItem(Material.AIR) {
                    @Override
                    public void populateForPlayer(MysteryPlayer player) {

                    }

                    @Override
                    public void onUse(MysteryPlayer player) {

                    }

                    @Override
                    public void onDrop(MysteryPlayer player) {

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

        MysteryItem bundleItem = new MysteryItem(material, MysteryItemOrigin.CONTAINER) {
            @Override
            public void populateForPlayer(MysteryPlayer player) {
                // Optional dynamic population logic
            }

            @Override
            public void onUse(MysteryPlayer player) {
                player.sendMessage("Viewing details for bundle: " + bundle.getName());
            }

            @Override
            public void onDrop(MysteryPlayer player) {
                // Prevent dropping
            }
        };
        bundleItem.setName(name);
        bundleItem.setLore(lore);
        bundleItem.setItemIdentifier(bundle.getName() + "_BUNDLE");
        setItem(slot, bundleItem);
    }

    private String summarize(List<String> items, int max) {
        if (items == null || items.isEmpty()) return "<gray>None</gray>";
        return items.size() > max
                ? String.join(", ", items.subList(0, max)) + "..."
                : String.join(", ", items);
    }
}
