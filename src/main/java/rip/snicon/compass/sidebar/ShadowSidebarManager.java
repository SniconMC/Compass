package rip.snicon.compass.sidebar;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceManager;
import rip.snicon.compass.player.ShadowPlayer;
import rip.snicon.compass.utils.LoadJSON;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShadowSidebarManager {

    private final File dataFolder = new File("resources/sidebar");

    private Map<String, ShadowSidebar> shadowSidebars = new HashMap<>();

    public void Init(){

        Map<String, String> sidebars = new LoadJSON().load(dataFolder);

        for (String sidebar : sidebars.keySet()) {

            // Load ShadowSidebar from JSON
            ShadowSidebar shadowSidebar = ShadowSidebar.fromJson(sidebars.get(sidebar));

            shadowSidebars.put(sidebar ,shadowSidebar);
        }
    }

    public void setSidebar(ShadowPlayer player, String name) {
        ShadowSidebar sidebar = shadowSidebars.get(name);
        sidebar.addViewer(player);
        player.setViewingSidebar(sidebar);
    }
}
