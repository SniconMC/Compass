package rip.snicon.modules.container;

import com.github.sniconmc.utils.item.ItemStackBuilder;
import com.github.sniconmc.utils.item.MaterialUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import rip.snicon.Main;
import rip.snicon.modules.container.json.InventorySettings;
import rip.snicon.modules.container.json.Item;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotbarCreator {

    private static File dataFolder;
    private static Gson gson;
    private static Map<String, ContainerConfig> configMap;

    public HotbarCreator() {
        dataFolder = new File("resources/container/hotbars");
        gson = new GsonBuilder().setPrettyPrinting().create();
        configMap = new HashMap<>();
        loadHotbars();
    }

    private static void loadHotbars() {
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
        try (FileReader reader = new FileReader(file)) {
            ContainerConfig containerConfig = gson.fromJson(reader, ContainerConfig.class);
            String name = file.getName().replace(".json", "");
            configMap.put(name, containerConfig);
        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading container file: " + file.getName());
        }
    }

    public static void reloadHotbars(){
        loadHotbars();
    }

    public static void setHotbar(Player player, String name){
        ContainerConfig config = configMap.get(name);
        if (config == null) {
            Main.logger.warn("Hotbar '" + name + "' not found, skipping");
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        List<Item> items = config.getItems();
        InventorySettings inventorySettings = config.getInventorySettings();

        Map<Integer, Item> itemMap = new HashMap<>();
        for (Item item : items) {
            itemMap.put(item.getSlot(), item);
        }
        for (int slot = 0; slot < playerInventory.getInnerSize(); slot++) {
            Item item = itemMap.get(slot);

            if (item != null) {
                item.setContainerId(inventorySettings.getContainerID());
                ItemStack itemInSlot = new ItemStackBuilder().material(MaterialUtils.convertToNamespaceIdMaterial(item.getId())).count(item.getCount().getCurrent()).build();
                playerInventory.setItemStack(item.getSlot(), itemInSlot);
            }
        }
    }
}
