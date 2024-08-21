package rip.snicon.modules.container;

import rip.snicon.modules.container.json.InventorySettings;
import rip.snicon.modules.container.json.Item;

import java.util.List;

public class ContainerConfig {

    private List<Item> items;
    private Item default_item;
    private InventorySettings settings;

    public Item getDefault_item() {
        return default_item;
    }

    public InventorySettings getInventorySettings() {
        return settings;
    }

    public void setInventorySettings(InventorySettings inventorySettings) {
        this.settings = inventorySettings;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Item getItem(int index){
        if (index < 0 || index >= items.size()){
            return null;
        }
        return items.get(index);
    }
    public Item setItem(int index, Item item){
        if (index < 0 || index >= items.size()){
            return null;
        }
        return items.set(index, item);
    }
}
