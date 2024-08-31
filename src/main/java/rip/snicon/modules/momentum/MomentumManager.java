package rip.snicon.modules.momentum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import rip.snicon.Main;
import rip.snicon.modules.oblivion.entity.DisplayText;
import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class MomentumManager {


    private static final Map<String, File> momentumDataFolders = new HashMap<>();
    private static Gson gson;

    private static final Map<String, Map<String, String>> momentumMap = new HashMap<>();
    private static final Map<Player, Map<String, List<Entity>>> displayMap = new HashMap<>();
    private static final Map<Player, Map<String, Map<String, MomentumConfig>>> configMap = new HashMap<>();

    public MomentumManager() {
        momentumMap.put("launchpads", new HashMap<>());
        momentumMap.put("telepads", new HashMap<>());

        File launchpadDataFolder = new File("resources/momentum/launchpads");
        File telepadDataFolder = new File("resources/momentum/telepads");

        momentumDataFolders.put("launchpads", launchpadDataFolder);
        momentumDataFolders.put("telepads", telepadDataFolder);

        gson = new GsonBuilder().setPrettyPrinting().create();

        loadMomentum();
    }

    private static void loadMomentum() {
        // A set to store names of all files processed during this reload
        Set<String> processedFiles = new HashSet<>();

        for (String name : momentumMap.keySet()) {
            File dataFolder = momentumDataFolders.get(name);
            Map<String, String> dataMap = momentumMap.get(name);
            dataMap.clear(); // Clear the existing data to remove outdated entries

            if (dataFolder.exists() && dataFolder.isDirectory()) {
                // Start searching from the containerFolder and add processed files to the set
                searchFiles(dataFolder, name, processedFiles);
            } else {
                Main.logger.warn("the '{}' folder does not exist! Creating...", name);
                boolean hasCreated = dataFolder.mkdirs();

                if (hasCreated) {
                    Main.logger.info("Created momentum folder '{}'!", name);
                } else {
                    Main.logger.warn("Failed to create momentum folder '{}'!", name);
                }

            }

            // Remove entries from configMap for files that no longer exist
            removeDeletedConfigurations(name, processedFiles);
        }
    }

    private static void searchFiles(File folder, String name, Set<String> processedFiles) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively search in subdirectories
                    searchFiles(file, name, processedFiles);
                } else if ( file.isFile() && file.getName().endsWith(".json")) {
                    // Process JSON files
                    processJsonFile(file, name);
                    processedFiles.add(file.getName().replace(".json", "")); // Add the file name to the processed list
                }
            }
        } else {
            Main.logger.error("The {} config file does not exist!", name);
        }
    }

    private static void removeDeletedConfigurations(String name, Set<String> processedFiles) {
        // Remove files from momentumMap that are not in the processedFiles set
        momentumMap.get(name).keySet().removeIf(fileName -> !processedFiles.contains(fileName));

        // Clear player-specific configurations for deleted files
        for (Player player : configMap.keySet()) {
            Map<String, Map<String, MomentumConfig>> playerConfig = configMap.get(player);
            if (playerConfig.containsKey(name)) {
                Map<String, MomentumConfig> momentumConfigs = playerConfig.get(name);
                momentumConfigs.keySet().removeIf(fileName -> !processedFiles.contains(fileName));
            }
        }
    }


    private static void processJsonFile(File file, String name) {
        try {
            String config = new String(Files.readAllBytes(file.toPath()));
            String fileName = file.getName().replace(".json", "");
            Map<String, String> dataMap = momentumMap.get(name);
            dataMap.put(fileName, config);
        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: {}", file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading '{}' file: {}", name, file.getName());
        }
    }

    public static void reloadMomentumPads() {
        loadMomentum();

        Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();
        for (Player player : players) {
            despawnText(player);
            createMomentumPads(player);
        }
    }

    public static void createMomentumPads(Player player){

        for (String name : momentumMap.keySet()) {

            Map<String, String> dataMap = momentumMap.get(name);
            for (String fileName : dataMap.keySet()) {
                String momentumJson = dataMap.get(fileName);
                String placeholdedJson = PlaceholderReplacer.replacePlaceholders(player, momentumJson);
                try {
                    MomentumConfig config = gson.fromJson(placeholdedJson, MomentumConfig.class);

                    Map<String, Map<String, MomentumConfig>> momentumConfigMapMap = configMap.getOrDefault(player, new HashMap<>());
                    Map<String, MomentumConfig> momentumConfigMap = momentumConfigMapMap.getOrDefault(name, new HashMap<>());

                    momentumConfigMap.put(fileName, config);
                    momentumConfigMapMap.put(name, momentumConfigMap);
                    configMap.put(player, momentumConfigMapMap);

                    spawnText(player, config, fileName);
                } catch (JsonSyntaxException | JsonIOException e) {
                    // Handle Gson-specific errors
                    Main.logger.error("Error parsing JSON in: {}", fileName);
                } catch (Exception e) {
                    // Handle any other unexpected exceptions
                    Main.logger.error("Unexpected error in: {}", fileName);
                }
            }
        }
    }


    public static void spawnText(Player player, MomentumConfig config, String name) {
        Map<String, List<Entity>> entityMap = displayMap.getOrDefault(player, new HashMap<>()); // Retrieve existing map for player
        List<Entity> entities = new ArrayList<>();

        int size = config.getDisplay().getText_list().size();
        for (int i = 0; i < size; i++) {
            List<String> text = config.getDisplay().getText_list().get(size - 1 - i);
            DisplayText textDisplay = new DisplayText(i, config.getCorners().getMiddle(), text);
            textDisplay.makeVisibleTo(player);
            entities.add(textDisplay);
        }

        entityMap.put(name, entities); // Add or overwrite the entities list for this name
        displayMap.put(player, entityMap); // Update the displayMap with the new entity map

    }

    public static void despawnText(Player player) {
        Map<String, List<Entity>> entityMap = displayMap.get(player);
        if (entityMap == null) {
            Main.logger.info("No display name found for this pad. Assuming intentional...");
            return;
        }

        for (String name : entityMap.keySet()) {
            List<Entity> entities = entityMap.get(name);
            if (entities == null || entities.isEmpty()) {
                continue;
            }

            for (Entity entity : entities) {
                if (entity instanceof DisplayText textDisplay) {
                    textDisplay.despawnForPlayer(player);
                } else {
                    Main.logger.warn("Entity not of type DisplayText: {}", entity.getEntityId());
                }
            }
        }

        displayMap.remove(player); // Optionally clear the player's entities after despawning
    }




    public static Map<Player, Map<String, Map<String, MomentumConfig>>> getConfigMap() {
        return configMap;
    }
}
