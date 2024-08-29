package rip.snicon.modules.sidebar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import rip.snicon.Main;
import rip.snicon.utils.PlaceholderReplacer;
import rip.snicon.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class SidebarCreator {

    private static File dataFolder;
    private static Gson gson;
    private static Map<String, String> playerSidebarMap;
    private static Map<String, Sidebar> sidebarMap;


    public SidebarCreator() {
        dataFolder = new File("resources/sidebar");
        gson = new GsonBuilder().setPrettyPrinting().create();
        sidebarMap = new HashMap<>();
        playerSidebarMap = new HashMap<>();
        loadPlayerSidebars();
        createSidebarsFromJson();
    }

    private static void loadPlayerSidebars() {
        playerSidebarMap.clear();
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
            String playerSidebar = new String(Files.readAllBytes(file.toPath()));
            String name = file.getName().replace(".json", "");
            playerSidebarMap.put(name, playerSidebar);
        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading Sidebar file: " + file.getName());
        }
    }

    private void createSidebarsFromJson() {
        for (String name : playerSidebarMap.keySet()) {
            Sidebar sidebar = new Sidebar(Component.text(name));

            sidebarMap.put(name, sidebar);
        }
    }

    public static void reloadSidebars(){
        loadPlayerSidebars();
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            updateSidebar(player);
        }
    }

    public static void updateSidebar(Player player) {

        for (String name : playerSidebarMap.keySet()) {
            Sidebar sidebar = sidebarMap.get(name);
            String sidebarJson = playerSidebarMap.get(name);
            String placeholdedJson = PlaceholderReplacer.replacePlaceholders(player, sidebarJson);
            try {

                PlayerSidebar playerSidebar = gson.fromJson(placeholdedJson, PlayerSidebar.class);

                sidebar.setTitle(TextUtils.convertStringToComponent(
                        playerSidebar.getTitle())
                );

                List<List<String>> layout = playerSidebar.getLayout();

                for (List<String> text : layout) {
                    int number = layout.size() - layout.indexOf(text);
                    String id = "row" + number;

                    // Remove the existing line if it exists
                    sidebar.removeLine(id);

                    // Create updated content
                    Component updatedContent = TextUtils.convertStringToComponent(text);

                    // Create a new line with updated content
                    Sidebar.ScoreboardLine updatedLine = new Sidebar.ScoreboardLine(
                            id,
                            updatedContent,
                            number,
                            Sidebar.NumberFormat.blank()
                    );

                    // Add the updated line to the sidebar
                    sidebar.createLine(updatedLine);
                }

            } catch (JsonSyntaxException | JsonIOException e) {
                // Handle Gson-specific errors
                Main.logger.error("Error parsing JSON in: " + name);
            } catch (Exception e) {
                // Handle any other unexpected exceptions
                Main.logger.error("Unexpected error in: " + name);
            }

        }
    }

    public static void setSidebar(Player player, String sidebarName) {
        // Remove the player from any other sidebar
        for (Sidebar sidebar : sidebarMap.values()) {
            sidebar.removeViewer(player);
        }

        // Get the specified sidebar
        Sidebar sidebar = sidebarMap.get(sidebarName);

        // If the sidebar doesn't exist, create a new one
        if (sidebar == null) {
            Main.logger.error(sidebarName + " is not a valid sidebar!");
            return;
        }

        // Add the player as a viewer to the specified sidebar
        sidebar.addViewer(player);
        updateSidebar(player);
    }

    public static Map<String, Sidebar> getSidebarMap() {
        return sidebarMap;
    }

    public static Map<String, String> getPlayerSidebarMap() {
        return playerSidebarMap;
    }
}
