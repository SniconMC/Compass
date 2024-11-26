package rip.snicon.compass.instances;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import rip.snicon.compass.utils.LoadJSON;

import java.io.File;
import java.util.Map;

public class ShadowInstanceCreator {

    private final File dataFolder = new File("resources/worlds");

    public void Create(){
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        Map<String, String> instances = new LoadJSON().load(dataFolder);

        for (String instance : instances.keySet()) {

            // Define the AnvilLoader (or other IChunkLoader)
            AnvilLoader loader = new AnvilLoader(dataFolder + "/" + instance);

            // Load ShadowInstance from JSON
            ShadowInstance shadowInstance = ShadowInstance.fromJson(instances.get(instance), loader);

            // Initialize the instance with its settings
            shadowInstance.initialize();

            instanceManager.registerInstance(shadowInstance);

        }

    }

}
