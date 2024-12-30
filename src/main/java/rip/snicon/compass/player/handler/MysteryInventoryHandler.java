package rip.snicon.compass.player.handler;

import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.Main;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.inventory.MysteryInventoryType;
import rip.snicon.compass.inventory.item.MysteryItemTags;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MysteryInventoryHandler {

    private final UUID uuid;
    private final Map<Integer, MysteryItemType> inventory =  new HashMap<>(MysteryInventoryType.DEFAULT.getStaticInventory().getItems());;


    public MysteryInventoryHandler(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    // Fetch inventory data from the database
    public void fetchInventoryFromDatabase() {
        MongoDatabaseManager.fetch("inventories", "uuid", uuid.toString()).thenAccept(document -> {
            if (document != null) {
                loadInventoryData(document);
            } else {
                saveInventoryToDatabase();
            }
        });
    }

    // Save inventory data to the database
    public void saveInventoryToDatabase() {
        // Wrap inventory data in the "inventory" field
        Document inventoryDocument = new Document("uuid", uuid.toString());

        // Create a sub-document for the inventory items and add it to the "inventory" field
        Document inventoryData = new Document();
        inventory.forEach((slot, itemType) -> inventoryData.append(String.valueOf(slot), itemType.name()));

        // Save the inventory data under the "inventory" field
        inventoryDocument.append("inventory", inventoryData);

        MongoDatabaseManager.save("inventories", "uuid", inventoryDocument).exceptionally(throwable -> {
            Main.logger.error("Failed to save inventory for UUID: " + uuid.toString(), throwable);
            return null;
        });
    }

    // Load inventory data from a database document
    private void loadInventoryData(Document document) {
        if (document.containsKey("inventory")) {
            Document inventoryData = document.get("inventory", Document.class);

            for (String slotKey : inventoryData.keySet()) {
                try {
                    int slot = Integer.parseInt(slotKey);

                    // Ensure the slot is within a valid range (optional, depending on your requirements)
                    if (slot >= 0 && slot < PlayerInventory.INNER_INVENTORY_SIZE) {
                        MysteryItemType itemType = MysteryItemType.valueOf(inventoryData.getString(slotKey));
                        inventory.put(slot, itemType);
                    }
                } catch (NumberFormatException e) {
                    Main.logger.error("Invalid slot key in database: " + slotKey);
                } catch (IllegalArgumentException e) {
                    Main.logger.error("Invalid item type in database for slot: " + slotKey);
                }
            }
        } else {
            Main.logger.warn("No inventory data found for UUID: " + uuid.toString());
        }
    }

    // Load items into the player's inventory
    public void loadInventory(PlayerInventory playerInventory) {

        inventory.forEach((slot, itemType) -> {
            if (itemType != null) {
                playerInventory.setItemStack(slot, itemType.getItem(MysteryPlayer.getPlayer(uuid)).createItemStack());
            }
        });
    }

    // Save items from the player's inventory
    public void saveInventory(PlayerInventory playerInventory) {
        for (int slot = 0; slot < playerInventory.getInnerSize(); slot++) {
            ItemStack itemStack = playerInventory.getItemStack(slot);
            String itemIdentifier = itemStack.getTag(Tag.String(MysteryItemTags.ITEM_IDENTIFIER.name()));

            if (itemIdentifier != null) {
                MysteryItemType itemType = MysteryItemType.valueOf(itemIdentifier);
                inventory.put(slot, itemType);
            } else {
                inventory.remove(slot);
            }
        }
        saveInventoryToDatabase();
    }

    // Add an item to the first available slot
    public boolean addItem(PlayerInventory playerInventory, MysteryItemType itemType) {
        for (int slot = 0; slot < playerInventory.getInnerSize(); slot++) {
            if (playerInventory.getItemStack(slot).isAir()) {
                playerInventory.setItemStack(slot, itemType.getItem(MysteryPlayer.getPlayer(uuid)).createItemStack());
                inventory.put(slot, itemType);
                saveInventoryToDatabase();
                return true;
            }
        }
        return false; // No available slot
    }

    // Remove an item from the inventory
    public void removeInventoryItem(int slot) {
        inventory.remove(slot);
        saveInventoryToDatabase();
    }

    // Get an item from a specific slot
    public MysteryItemType getInventoryItem(int slot) {
        return inventory.get(slot);
    }

    // Get the full inventory
    public Map<Integer, MysteryItemType> getFullInventory() {
        return new HashMap<>(inventory); // Return a copy to ensure encapsulation
    }
}
