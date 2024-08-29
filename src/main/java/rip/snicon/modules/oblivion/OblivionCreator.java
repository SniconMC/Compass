package rip.snicon.modules.oblivion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import rip.snicon.Main;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.modules.oblivion.entity.DisplayText;
import rip.snicon.modules.oblivion.entity.NPC;
import rip.snicon.modules.oblivion.json.Oblivion;
import rip.snicon.utils.EntityUtils;
import rip.snicon.utils.PlaceholderReplacer;
import rip.snicon.utils.SkinUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class OblivionCreator {

    private static File dataFolder;
    private static Gson gson;
    private static Map<String, String> oblivionConfigs;
    private static final Map<Player, Oblivion> oblivionFakes = new HashMap<>();

    public OblivionCreator(){
        dataFolder = new File("resources/oblivions");
        gson = new GsonBuilder().setPrettyPrinting().create();
        oblivionConfigs = new HashMap<>();
        loadOblivions();
        createHiddenNameTeam();
    }

    private static void loadOblivions() {
        oblivionConfigs.clear();
        if (dataFolder.exists() && dataFolder.isDirectory()) {
            // Start searching from the containerFolder
            searchFiles(dataFolder);
        } else {
            Main.logger.warn("the worlds dataFolder folder does not exist! Creating...");
            boolean hasCreated = dataFolder.mkdirs();

            if (hasCreated) {
                Main.logger.info("Created dataFolder!");
            } else {
                Main.logger.warn("Failed to create dataFolder!");
            }
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
        } else {
            Main.logger.error("The Oblivion config file does not exist!");
        }
    }

    private static void processJsonFile(File file) {
        try {
            String oblivionConfig = new String(Files.readAllBytes(file.toPath()));
            String name = file.getName().replace(".json", "");
            oblivionConfigs.put(name, oblivionConfig);
        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading Oblivion file: " + file.getName());
        }
    }

    public static void reloadOblivions(){
        loadOblivions();
        Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();
        for (Player player : players) {
            despawnOblivions(player);
            spawnOblivions(player);
        }
    }

    public static void spawnOblivions(Player player){
        Oblivion oblivion = new Oblivion();
        Map<String, NPC> NpcMap = new HashMap<>();
        Map<String, List<Entity>> textDisplayMap = new HashMap<>();
        for (String name : oblivionConfigs.keySet()) {

            String oblivionJson = oblivionConfigs.get(name);
            String placeholdedJson = PlaceholderReplacer.replacePlaceholders(player, oblivionJson);
            try {
                // Parse the JSON string into an OblivionConfig object
                OblivionConfig config = gson.fromJson(placeholdedJson, OblivionConfig.class);

                Instance instance = InstanceCreator.getInstanceMap().get(config.getWorld());
                Pos pos = new Pos(config.getPosition().getX(), config.getPosition().getY(), config.getPosition().getZ(), config.getPosition().getYaw(), config.getPosition().getPitch());
                PlayerSkin skin = SkinUtils.getSkin(player, config.getSkin().getPlayer(), "", config.getSkin().getTexture(), config.getSkin().getSignature());

                // Create and spawn the NPC, passing the method reference for onClick
                NPC npc = new NPC(name, skin, instance,  pos, OblivionCreator::handleNpcClick, config, EntityUtils.getEntityTypeFromNamespace(config.getEntity_type()));
                npc.makeVisibleTo(player);
                NpcMap.put(name, npc);

                Pos namePos = pos.add(0, npc.getEyeHeight() + 0.45, 0);

                List<Entity> entities = new ArrayList<>();
                int size = config.getName().size();
                for (int i = 0; i < size; i++) {
                    DisplayText textDisplay = new DisplayText(i, namePos,  config.getName().get((size-1)-i));
                    textDisplay.makeVisibleTo(player);

                    entities.add(textDisplay);
                }
                textDisplayMap.put(name, entities);



            } catch (JsonSyntaxException | JsonIOException e) {
                // Handle Gson-specific errors
                Main.logger.error("Error parsing JSON in: " + name);
            } catch (Exception e) {
                // Handle any other unexpected exceptions
                Main.logger.error("Unexpected error in: " + name);
            }
            oblivion.setOblivions(NpcMap);
            oblivion.setOblivionsName(textDisplayMap);
            oblivionFakes.put(player, oblivion);

        }
    }

    public static void despawnOblivions(Player player) {
        Oblivion oblivion = oblivionFakes.get(player);
        if (oblivion == null) {
            return;
        }
        for (String name : oblivion.getOblivions().keySet()) {
            NPC npc = oblivion.getOblivions().get(name);
            npc.despawnForPlayer(player);

            List<Entity> displays =  oblivion.getOblivionsName().get(name);
            for (Entity entity : displays) {
                DisplayText textDisplay = (DisplayText) entity;
                textDisplay.despawnForPlayer(player);
            }
        }
    }


    private static void createHiddenNameTeam() {
        Team hiddenName = MinecraftServer.getTeamManager().createTeam("hidden_name");
        hiddenName.setNameTagVisibility(TeamsPacket.NameTagVisibility.NEVER);
    }
    // Method to handle NPC click interactions
    public static void handleNpcClick(Player player) {
        // TODO: NPC click
        player.sendMessage("hehe");
    }

}
