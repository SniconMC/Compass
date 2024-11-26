package rip.snicon.compass;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.compass.instances.ShadowInstanceCreator;
import rip.snicon.compass.listeners.Global;
import rip.snicon.compass.sidebar.ShadowSidebarManager;


public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        new ShadowInstanceCreator().Create();
        new ShadowSidebarManager().Init();
         // Listener
        new Global();

        // Start the server
        int port = Integer.parseInt(System.getenv().getOrDefault("SERVER_PORT", "25565"));
        minecraftServer.start("0.0.0.0", port);
        Main.logger.info("Server starting on port: {}", port);
    }
}
