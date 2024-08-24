package rip.snicon.modules.container;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import rip.snicon.Main;
import rip.snicon.modules.container.json.InventorySettings;
import rip.snicon.modules.container.json.Item;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rip.snicon.utils.PlaceholderReplacer;
import rip.snicon.utils.TextUtils;
import rip.snicon.utils.inventory.InventoryUtils;
import rip.snicon.utils.item.ItemStackUtils;


import static rip.snicon.utils.TextUtils.convertToComponentWithPlaceholders;


public class ContainerCreator {

    private static File dataFolder;
    private static Gson gson;
    private static Map<String, String> configMap;

    public ContainerCreator() {
        dataFolder = new File("resources/container/containers");
        gson = new GsonBuilder().setPrettyPrinting().create();
        configMap = new HashMap<>();
        loadContainers();
    }

    private static void loadContainers() {
        configMap.clear();
        if (dataFolder.exists() && dataFolder.isDirectory()) {
            // Start searching from the containerFolder
            searchFiles(dataFolder);
        } else {
            Main.logger.error("the worlds dataFolder does not exist!");
        }
    }

    private static void searchFiles(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively search in subdirectories
                    searchFiles(file);
                } else if (file.isFile() && file.getName().endsWith(".json")) {
                    // Process JSON files
                    processJsonFile(file);
                }
            }
        }
    }

    private static void processJsonFile(File file) {
        try {
            String containerJson = new String(Files.readAllBytes(file.toPath()));

            String name = file.getName().replace(".json", "");
            configMap.put(name, containerJson);

            Main.logger.info("Loaded Container config: " + name);

        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading container file: " + file.getName());
        }
    }

    public static void reloadContainers() {
        loadContainers();
    }

    public static void openContainer(Player player, String name) {


        String containerJson = configMap.get(name);
        String placeholdedJson = PlaceholderReplacer.replacePlaceholders(player, containerJson);
        Main.logger.info(placeholdedJson);
        try {

            ContainerConfig config = gson.fromJson(placeholdedJson, ContainerConfig.class);
            if (config == null) {
                Main.logger.error("Container not found: " + name);
                return;
            }

            Component displayName = convertToComponentWithPlaceholders(config.getInventorySettings().getDisplayName(), player);
            InventoryType inventoryType = InventoryUtils.getInventoryType(config.getInventorySettings().getRow());
            int size = config.getInventorySettings().getRow() * 9;
            Inventory inventory = new Inventory(inventoryType, displayName);

            List<Item> items = config.getItems();
            InventorySettings inventorySettings = config.getInventorySettings();

            Map<Integer, Item> itemMap = new HashMap<>();
            for (Item item : items) {
                itemMap.put(item.getSlot(), item);
            }

            for (int slot = 0; slot < size; slot++) {
                Item item = itemMap.get(slot);
                if (item != null) {
                    item.setContainerId(inventorySettings.getContainerID());
                    ItemStack containerItem = ItemStackUtils.createItemStack(item, player, inventorySettings);
                    inventory.setItemStack(slot, containerItem);
                } else {
                    Item defualtItem = config.getDefault_item();
                    defualtItem.setContainerId(inventorySettings.getContainerID());
                    ItemStack containerItem = ItemStackUtils.createItemStack(defualtItem, player, inventorySettings);
                    inventory.setItemStack(slot, containerItem);
                }
            }

            // Open inventory for player
            player.openInventory(inventory);
        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + name);
        }
    }
}