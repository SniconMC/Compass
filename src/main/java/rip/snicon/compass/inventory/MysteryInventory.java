package rip.snicon.compass.inventory;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class MysteryInventory {
    private final String title;
    private final Map<Integer, MysteryItemType> staticItems = new HashMap<>();
    private final Map<Integer, MysteryItem> dynamicItems = new HashMap<>();

    public MysteryInventory(String title) {
        this.title = title;
        initialize();
    }

    protected abstract void initialize();

    protected abstract void populate(MysteryPlayer player);

    public String getTitle() {
        return title;
    }

    /**
     * Sets a static item using the MysteryItemType enum.
     */
    protected void setItem(int slot, MysteryItemType item) {
        staticItems.put(slot, item);
    }


    /**
     * Sets a dynamic item using a MysteryItem instance.
     */
    protected void setItem(int slot, MysteryItem item) {
        dynamicItems.put(slot, item);
    }

    /**
     * Converts the MysteryInventory into a Minestom Inventory.
     */
    public Inventory toMinestomInventory(MysteryPlayer player, InventoryType type) {
        Inventory inventory = new Inventory(type, TextUtils.convertStringToComponent(title));

        // Populate static items
        for (Map.Entry<Integer, MysteryItemType> entry : staticItems.entrySet()) {
            int slot = entry.getKey();
            MysteryItemType item = entry.getValue();
            if (item != null) {
                inventory.setItemStack(slot, item.getItem(player).createItemStack());
            }
        }

        // Populate dynamic items
        for (Map.Entry<Integer, MysteryItem> entry : dynamicItems.entrySet()) {
            int slot = entry.getKey();
            MysteryItem item = entry.getValue();
            if (item != null) {
                inventory.setItemStack(slot, item.createItemStack());
            }
        }

        return inventory;
    }

    /**
     * Converts the MysteryInventory into a Minestom Inventory with a filler item.
     */
    /**
     * Converts the MysteryInventory into a Minestom Inventory with a filler MysteryItem.
     */
    public Inventory toMinestomInventory(MysteryPlayer player, InventoryType type, MysteryItem fill) {
        Inventory inventory = new Inventory(type, TextUtils.convertStringToComponent(title));

        // Populate static items
        for (Map.Entry<Integer, MysteryItemType> entry : staticItems.entrySet()) {
            int slot = entry.getKey();
            MysteryItemType item = entry.getValue();
            if (item != null) {
                inventory.setItemStack(slot, item.getItem(player, inventory, slot).createItemStack());
            }
        }

        // Populate dynamic items
        for (Map.Entry<Integer, MysteryItem> entry : dynamicItems.entrySet()) {
            int slot = entry.getKey();
            MysteryItem item = entry.getValue();
            if (item != null) {
                item.setHosts(inventory, slot);
                inventory.setItemStack(slot, item.createItemStack());
            }
        }

        // Fill remaining slots with the MysteryItem
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.getItemStack(i);
            if (inventory.getItemStack(i).isAir()) {
                inventory.setItemStack(i, fill.createItemStack());
            }
        }

        return inventory;
    }


    public Map<Integer, MysteryItemType> getItems() {
        return staticItems;
    }
}
