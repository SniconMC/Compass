package rip.snicon.compass.sidebar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.player.ShadowPlayer;
import rip.snicon.compass.utils.TextUtils;

public class ShadowSidebar extends Sidebar {

    private JsonArray layout; // Stores the layout for initialization

    private boolean defaultSidebar;
    // Constructor
    public ShadowSidebar(Component title) {
        super(title);
    }


    // Method to populate fields from JSON
    public void populateFromJson(JsonObject json) {
        this.defaultSidebar = json.get("defaultSidebar").getAsBoolean();
    }

    // Initialize the sidebar lines based on the layout
    private void initialize() {
        if (layout == null) {
            throw new IllegalStateException("Layout has not been set. Ensure layout is properly parsed before initialization.");
        }

        for (int i = 0; i < layout.size(); i++) {
            String line = layout.get(i).getAsString();
            Component content = TextUtils.convertStringToComponent(line);

            // Add each line with the correct index
            this.createLine(new Sidebar.ScoreboardLine("line_" + i, content, layout.size() - i));
        }

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            if (event.getPlayer() instanceof ShadowPlayer player) {
                if (this.defaultSidebar) {
                    this.addViewer(player);
                    player.setViewingSidebar(this);
                }
            }


        });

    }

    // Factory method to create a ShadowSidebar from a JSON string
    public static ShadowSidebar fromJson(String jsonContent) {
        try {
            // Parse JSON content
            JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();

            // Extract title
            Component title = TextUtils.convertStringToComponent(jsonObject.get("title").getAsString());
            ShadowSidebar sidebar = new ShadowSidebar(title);

            // Extract layout and set it
            sidebar.layout = jsonObject.getAsJsonArray("layout");

            // Populate the instance fields with JSON data
            sidebar.populateFromJson(jsonObject);

            // Automatically initialize the sidebar
            sidebar.initialize();

            return sidebar;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create ShadowSidebar: " + e.getMessage(), e);
        }
    }
}
