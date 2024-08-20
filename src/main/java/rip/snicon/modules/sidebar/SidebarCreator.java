package rip.snicon.modules.sidebar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import rip.snicon.utils.TextUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SidebarCreator {

    private final File dataFolder;
    private final Gson gson;
    private Map<String, PlayerSidebar> playerSidebarMap;
    private Map<String, Sidebar> sidebarMap;
    private Map<Player, Map<String, String>> playerPlaceholders;

    public SidebarCreator() {
        this.dataFolder = new File("resources/sidebar");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.sidebarMap = new HashMap<>();
        this.playerSidebarMap = new HashMap<>();
        loadPlayerSidebars();
        createSidebarsFromJson();
    }

    private void loadPlayerSidebars() {
        sidebarMap.clear();
        if (dataFolder.exists() && dataFolder.isDirectory()) {
            // Start searching from the containerFolder
            searchFiles(dataFolder);
        } else {
            System.out.println("the worlds dataFolder does not exist");
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
            PlayerSidebar playerSidebar = gson.fromJson(reader, PlayerSidebar.class);
            String name = file.getName().replace(".json", "");
            playerSidebarMap.put(name, playerSidebar);

            System.out.println("Loaded world info:" + name);

        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            System.out.println("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            System.out.println("Error loading container file: " + file.getName());
        }
    }

    private void createSidebarsFromJson() {


        for (String name : playerSidebarMap.keySet()) {
            Sidebar sidebar = new Sidebar(Component.text("name"));

            PlayerSidebar playerSidebar = playerSidebarMap.get(name);
            sidebar.setTitle(TextUtils.convertToComponent(playerSidebar.getTitle()));

            sidebarMap.put(name, sidebar);
        }
    }
}
