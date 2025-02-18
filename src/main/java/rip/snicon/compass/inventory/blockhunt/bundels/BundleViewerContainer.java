package rip.snicon.compass.inventory.blockhunt.bundels;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import rip.snicon.compass.inventory.blockhunt.BlockhuntMenu;
import rip.snicon.compass.utils.TextUtils;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.bundle.MysteryBundle;
import rip.snicon.compass.player.handler.MysteryBundleHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static rip.snicon.compass.player.data.MysteryRarities.RARITY_PRIORITY;

public class BundleViewerContainer extends TemplateInventory {

    public BundleViewerContainer() {
        super(TextUtils.convertStringToComponent("Your Bundles"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());
        setItem(40, new CloseButton());
        setItem(36, new TemplateItem(Material.ARROW) {
            @Override
            protected void initialize() {
                setName(Component.text("Back"));
            }

            @Override
            protected void personalize(Player player) {}

            @Override
            public void onUse(TemplateInventoryEvent templateInventoryEvent) {
                templateInventoryEvent.getPlayer().openInventory(new BlockhuntMenu().constructInventory(templateInventoryEvent.getPlayer()));
            }

            @Override
            public void onDrop(TemplateInventoryEvent templateInventoryEvent) {}
        });
    }

    @Override
    protected void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;
        MysteryBundleHandler bundleHandler = p.getBundleHandler();
        List<MysteryBundle> bundles = bundleHandler.getAllBundles().stream()
                .sorted(
                        // 1. isInfinite: true först
                        Comparator.comparing(MysteryBundle::isInfinite, Comparator.reverseOrder())
                                // 2. amount: fallande ordning
                                .thenComparing(MysteryBundle::getAmount, Comparator.reverseOrder())
                                // 3. rarities: efter vår önskade prioritet
                                .thenComparing(b -> RARITY_PRIORITY.get(b.getRarity()))
                )
                .collect(Collectors.toList());



        int[] slots = getBundleSlots();

        for (int i = 0; i < slots.length; i++) {
            if (i < bundles.size()) {
                // Om det finns en MysteryBundle för den här platsen
                addBundleItem(slots[i], bundles.get(i), player);
            } else {
                // Om vi inte har fler bundles, fyll sloten med "air" (tom plats)
                setItem(slots[i], new TemplateItem(Material.AIR) {
                    @Override
                    protected void initialize() {}

                    @Override
                    protected void personalize(Player player) {}

                    @Override
                    public void onUse(TemplateInventoryEvent event) {}

                    @Override
                    public void onDrop(TemplateInventoryEvent event) {}
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
        TemplateItem bundleItem = new TemplateItem(Material.fromNamespaceId(bundle.getDisplayItem())) {
            @Override
            protected void initialize() {}

            @Override
            protected void personalize(Player player) {}

            @Override
            public void onUse(TemplateInventoryEvent event) {
                event.getPlayer().openInventory(new BundleContentViewerContainer(
                        Component.text(bundle.getName()),
                        getContentList(bundle)).constructInventory(event.getPlayer()));
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {}
        };
        bundleItem.setName(bundle.getDisplayNameComponent());
        List<Component> lore = bundle.getLoreComponent();

        lore.addAll(TextUtils.convertStringToComponent(List.of("", "<yellow>Click To View Contents</yellow>")));

        bundleItem.setLore(lore);
        setItem(slot, bundleItem);
    }

    private List<Material> getContentList(MysteryBundle bundle) {
        // Skapa en ny lista baserat på bundle.getMaterials(), så att inte original-listan påverkas
        List<Material> content = new ArrayList<>(bundle.getMaterials());
        // Lägg sedan till spawn-eggs
        content.addAll(MysteryBundle.getSpawnEggForEntity(bundle.getEntities()));
        return content;
    }

}
