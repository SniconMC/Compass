package rip.snicon.compass.inventory.blockhunt.bundels;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import nub.wi1helm.template.inventory.TemplateItem;
import rip.snicon.compass.Main;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BundleContentViewerContainer extends TemplateInventory {
    public BundleContentViewerContainer(Component name, List<Material> content) {
        super(name, InventoryType.CHEST_6_ROW);
        initializeContent(content);
    }

    private void initializeContent(List<Material> content) {
        fillInventory(new BackgroundItem());
        setItem(49, new CloseButton()); // Center close button

        int[] slots = getContentSlots();
        for (int i = 0; i < slots.length; i++) {
            if (i < content.size()) {
                setItem(slots[i], createContentItem(content.get(i)));
            } else {
                setItem(slots[i], createContentItem(null));
            }

        }
    }

    private int[] getContentSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    private TemplateItem createContentItem(Material itemName) {

        if (itemName == null) itemName = Material.AIR;

        TemplateItem item = new TemplateItem(itemName) {
            @Override
            protected void initialize() {}

            @Override
            protected void personalize(Player player) {}

            @Override
            public void onUse(TemplateInventoryEvent templateInventoryEvent) {

            }

            @Override
            public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

            }
        };
        String formattedName = itemName.name()
                .replace("_spawn_egg", "")
                .replace("_", " ")// Remove spawn egg suffix
                .replace("minecraft:", ""); // Remove namespace prefix

        item.setName(Component.text(capitalizeFirstLetter(formattedName)));

        return item;
    }

    @Override
    protected void initialize() {
        setItem(45, new TemplateItem(Material.ARROW) {
            @Override
            protected void initialize() {
                setName(Component.text("Back"));
            }

            @Override
            protected void personalize(Player player) {

            }

            @Override
            public void onUse(TemplateInventoryEvent templateInventoryEvent) {
                templateInventoryEvent.getPlayer().openInventory(new BundleViewerContainer().constructInventory(templateInventoryEvent.getPlayer()));
            }

            @Override
            public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

            }
        });
    }

    @Override
    protected void personalize(Player player) {

    }

    // Utility method to capitalize the first letter of all words
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;

        return Arrays.stream(input.split(" ")) // Split by spaces
                .map(word -> word.isEmpty() ? "" : word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()) // Capitalize each word
                .collect(Collectors.joining(" ")); // Rejoin with spaces
    }

}

