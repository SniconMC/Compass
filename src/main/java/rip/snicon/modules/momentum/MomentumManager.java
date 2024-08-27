package rip.snicon.modules.momentum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import rip.snicon.Main;
import rip.snicon.utils.PlaceholderReplacer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MomentumManager {


    private static final Map<String, File> momentumDataFolders = new HashMap<>();
    private static Gson gson;
    private static final Map<String, Map<String, String>> momentumMap = new HashMap<>();;
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

        for (String name : momentumMap.keySet()) {
            File dataFolder = momentumDataFolders.get(name);
            Map<String, String> dataMap = momentumMap.get(name);
            dataMap.clear();

            if (dataFolder.exists() && dataFolder.isDirectory()) {
                // Start searching from the containerFolder
                searchFiles(dataFolder, name);
            } else {
                Main.logger.error("the '" + name + "' folder does not exist! Creating...");
                dataFolder.mkdirs();
            }
        }


    }

    private static void searchFiles(File folder, String name) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively search in subdirectories
                    searchFiles(file, name);
                } else if (file.isFile() && file.getName().endsWith(".json")) {
                    // Process JSON files
                    processJsonFile(file, name);
                }
            }
        } else {
            Main.logger.error("The" + name + "config file does not exist!");
        }
    }

    private static void processJsonFile(File file, String name) {
        try {
            String config = new String(Files.readAllBytes(file.toPath()));
            String fileName = file.getName().replace(".json", "");
            Map<String, String> dataMap = momentumMap.get(name);
            dataMap.put(fileName, config);

            Main.logger.info("Loaded '" + name + "' config: " + file.getName());

        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading '" + name + "' file: " + file.getName());
        }
    }

    public static void reloadMomentumPads() {
        loadMomentum();

        Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();
        for (Player player : players) {

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

                    // wilhelm valde variabelnamnet, "Den håller en map per telepads/launchapds"
                    Map<String, Map<String, MomentumConfig>> momentumConfigMapMap = configMap.getOrDefault(player, new HashMap<>());
                    Map<String, MomentumConfig> momentumConfigMap = momentumConfigMapMap.getOrDefault(name, new HashMap<>());

                    momentumConfigMap.put(fileName, config);
                    momentumConfigMapMap.put(name, momentumConfigMap);
                    configMap.put(player, momentumConfigMapMap);

                } catch (JsonSyntaxException | JsonIOException e) {
                    // Handle Gson-specific errors
                    Main.logger.error("Error parsing JSON in: " + fileName);
                } catch (Exception e) {
                    // Handle any other unexpected exceptions
                    Main.logger.error("Unexpected error in: " + fileName);
                }
            }
        }
    }

    public static Map<Player, Map<String, Map<String, MomentumConfig>>> getConfigMap() {
        return configMap;
    }
}
