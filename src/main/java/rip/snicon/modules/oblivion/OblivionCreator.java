package rip.snicon.modules.oblivion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import rip.snicon.Main;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.modules.oblivion.json.Oblivion;
import rip.snicon.utils.SkullUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OblivionCreator {

    private static File dataFolder;
    private static Gson gson;
    private static Map<String, OblivionConfig> oblivionConfigs;
    private static Map<Player, Oblivion> oblivionFakes;

    public OblivionCreator(){
        dataFolder = new File("resources/oblivions");
        gson = new GsonBuilder().setPrettyPrinting().create();
        oblivionConfigs = new HashMap<>();
        loadOblivions();
    }

    private static void loadOblivions() {
        oblivionConfigs.clear();
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
            OblivionConfig oblivionConfig = gson.fromJson(reader, OblivionConfig.class);
            String name = file.getName().replace(".json", "");
            oblivionConfigs.put(name, oblivionConfig);

            Main.logger.info("Loaded container config: " + name);

        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading container file: " + file.getName());
        }
    }

    public static void reloadContainers(){
        loadOblivions();
    }

    public static void spawnOblivions(Player player){

        for (String name : oblivionConfigs.keySet()) {
            OblivionConfig config = oblivionConfigs.get(name);

            Instance instance = InstanceCreator.getInstanceMap().get(config.getWorld());
            Pos pos = new Pos(config.getPosition().getX(), config.getPosition().getY(), config.getPosition().getZ(), config.getPosition().getYaw(), config.getPosition().getPitch());
            PlayerSkin skin = SkullUtils.getSkin(player, config.getSkin().getPlayer(), "", config.getSkin().getTexture(), config.getSkin().getSignature());

            // Create and spawn the NPC, passing the method reference for onClick
            NPC npc = new NPC(name, skin, instance,  pos, OblivionCreator::handleNpcClick);
            npc.makeVisibleTo(player);
            Main.logger.error("balls");
        }
    }

    // Method to handle NPC click interactions
    public static void handleNpcClick(Player player) {
        player.sendMessage("Example: Kill yourself " + player.getUsername());
    }



}
