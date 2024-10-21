package rip.snicon.compass.instances;

import com.github.sniconmc.oblivion.instance.OblivionInstance;
import com.github.sniconmc.utils.time.TimeUtils;
import com.github.sniconmc.utils.weather.WeatherUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import rip.snicon.compass.Main;
import rip.snicon.compass.instances.utils.LoadInstances;
import rip.snicon.compass.instances.worlds.WorldInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InstanceCreator {

    private static InstanceCreator instance; // Singleton instance

    private final File dataFolder = new File("resources/worlds");
    private final Gson gson;
    private final Map<String, String> worldMap; // FileName to World JSON map
    private final Map<String, Instance> worldNameToInstanceMap = new HashMap<>(); // WorldName to Instance map

    // Private constructor to prevent external instantiation
    private InstanceCreator() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.worldMap = new LoadInstances().load(dataFolder);
        createInstancesFromJson();
    }

    // Public method to access the singleton instance
    public static synchronized InstanceCreator getInstance() {
        if (instance == null) {
            instance = new InstanceCreator();
        }
        return instance;
    }

    public void createInstancesFromJson() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        for (String worldName : worldMap.keySet()) {
            try {
                WorldInfo worldInfo = gson.fromJson(worldMap.get(worldName), WorldInfo.class);

                CustomInstanceContainer instanceContainer = new CustomInstanceContainer(
                        new AnvilLoader("resources/worlds/" + worldName), worldInfo);

                instanceContainer.setTime(TimeUtils.convertTime(worldInfo.getTime()));
                instanceContainer.setWeather(WeatherUtils.convertWeather(worldInfo.getWeather()));
                instanceContainer.setTimeRate(worldInfo.isDoDaylightCycle() ? 1 : 0);

                instanceManager.registerInstance(instanceContainer);
                worldNameToInstanceMap.put(worldName, instanceContainer);

            } catch (JsonSyntaxException | JsonIOException e) {
                Main.logger.error("Error parsing JSON in world file: {}", worldName, e);
            } catch (Exception e) {
                Main.logger.error("Unexpected error processing world file: {}", worldName, e.fillInStackTrace());
            }
        }
        Main.logger.info(worldNameToInstanceMap.toString());
        OblivionInstance.setInstanceMap(worldNameToInstanceMap);
    }

    public Instance getInstanceByWorldName(String worldName) {
        return worldNameToInstanceMap.get(worldName);
    }

    public Map<String, String> getWorldMap() {
        return worldMap;
    }
}
