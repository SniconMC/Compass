package rip.snicon.compass.inventory.blockhunt.bundels;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import rip.snicon.compass.utils.TextUtils;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.bundle.MysteryBundle;
import rip.snicon.compass.player.handler.MysteryBundleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BundleViewerContainer extends TemplateInventory {

    public BundleViewerContainer() {
        super(TextUtils.convertStringToComponent("Your Bundles"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());
        setItem(40, new CloseButton()); // Close button
    }

    @Override
    protected void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;
        MysteryBundleHandler bundleHandler = p.getBundleHandler();
        List<MysteryBundle> bundles = new ArrayList<>(bundleHandler.getAllBundles());
        int[] slots = getBundleSlots();

        for (int i = 0; i < slots.length; i++) {
            if (i < bundles.size()) {
                addBundleItem(slots[i], bundles.get(i), player);
            } else {
                setItem(slots[i], new TemplateItem(Material.AIR) {
                    @Override
                    protected void initialize() {

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
                });
            }
        }
    }

    private int[] getBundleSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        };
    }

    private void addBundleItem(int slot, MysteryBundle bundle, Player player) {
        Material material = Material.fromNamespaceId(bundle.getDisplayItem());
        String name = String.format(
                "<gradient:%s:%s>%s</gradient>",
                bundle.getRarity().getColor(),
                bundle.getRarity().getColor(),
                TextUtils.capitalizeFirstLetter(bundle.getName())
        );
        List<String> lore = List.of(
                String.format("<yellow>Rarity:</yellow> <gradient:%s>%s</gradient>",
                        bundle.getRarity().getColor(),
                        TextUtils.capitalizeFirstLetter(bundle.getRarity().name())),
                "<green>Click to show contents</green>"
        );

        TemplateItem bundleItem = new TemplateItem(material) {
            @Override
            protected void initialize() {}

            @Override
            protected void personalize(Player player) {}

            @Override
            public void onUse(TemplateInventoryEvent event) {
                event.getPlayer().openInventory(new BundleContentViewerContainer(
                        TextUtils.convertStringToComponent(bundle.getName()),
                        getContentList(bundle)).constructInventory(event.getPlayer()));
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {}
        };
        bundleItem.setName(TextUtils.convertStringToComponent(name));
        bundleItem.setLore(TextUtils.convertStringToComponent(lore));
        setItem(slot, bundleItem);
    }

    private List<Material> getContentList(MysteryBundle bundle) {
        List<Material> content = new ArrayList<>();
        content.addAll(bundle.getMaterials());
        content.addAll(MysteryBundle.getSpawnEggForEntity(bundle.getEntities()));
        return content;
    }
}