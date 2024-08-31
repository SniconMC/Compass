package rip.snicon.instances;

import com.github.sniconmc.utils.time.TimeUtils;
import com.github.sniconmc.utils.weather.WeatherUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import rip.snicon.Main;
import rip.snicon.instances.worlds.WorldInfo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InstanceCreator {

    private static Map<String, Instance> instanceMap;
    private static Map<String, WorldInfo> worldMap;

    private final File dataFolder;
    private final Gson gson;

    public InstanceCreator() {
        this.dataFolder = new File("resources/worlds");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        instanceMap = new HashMap<>();
        worldMap = new HashMap<>();
        loadWorldInfo();
        createInstancesFromJson();
    }

    private void loadWorldInfo() {
        instanceMap.clear();
        if (dataFolder.exists() && dataFolder.isDirectory()) {
            // Start searching from the containerFolder
            searchFiles(dataFolder);
        } else {
            Main.logger.error("The worlds dataFolder does not exist!");
        }
    }

    private void searchFiles(File folder) {
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

    private void processJsonFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            WorldInfo info = gson.fromJson(reader, WorldInfo.class);
            String name = file.getName().replace(".json", "");

            worldMap.put(name, info);
            Main.logger.info("Loaded world info: " + name);

        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading Instance file: " + file.getName());
        }
    }

    public void createInstancesFromJson() {
        // get instanceManager
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // loop all loaded world names
        for (String worldName : worldMap.keySet()) {

            WorldInfo info = worldMap.get(worldName);

            // create a new instance with the selected world (if failed to load will result in empty void world)
            InstanceContainer instanceContainer = instanceManager.createInstanceContainer(new AnvilLoader("resources/worlds/" + worldName));

            // time, weather and daylight cycle
            instanceContainer.setTime(TimeUtils.convertTime(info.getTime()));
            instanceContainer.setWeather(WeatherUtils.convertWeather(info.getWeather()));

            instanceContainer.setTimeRate(info.isDoDaylightCycle() ? 1 : 0);

            // save the instance with name
            instanceMap.put(worldName, instanceContainer);
        }
    }

    public static Map<String, Instance> getInstanceMap() {
        return instanceMap;
    }
    public static Map<String, WorldInfo> getWorldMap() {
        return worldMap;
    }
}
