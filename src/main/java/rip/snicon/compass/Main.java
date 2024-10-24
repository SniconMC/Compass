package rip.snicon.compass;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();
        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();

        // Set default port or use provided argument
        int port = 25565; // Default port

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                Main.logger.error("Invalid port number. Using default port 25565.");
            }
        }
        Main.logger.info("Server starting on port: {}",port);
        // Create the instance(world)
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        // Generate the world
        instanceContainer.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK);
        });

        // Add Lighting
        instanceContainer.setChunkSupplier(LightingChunk::new);

        // Schedule shutdown task
        scheduler.buildShutdownTask(() -> {
            Main.logger.info("Shutting down...");
        });

        // Global Event Handlers
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 3, 0));
        });
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            event.setCancelled(true);
        });

        // Start the server on the chosen port
        minecraftServer.start("0.0.0.0", port);
    }
}
