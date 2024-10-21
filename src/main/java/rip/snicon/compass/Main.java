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

        //Create the instance(world)
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        //Generate the world
        instanceContainer.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK);
        });
        //Add Lighting
        instanceContainer.setChunkSupplier(LightingChunk::new);

        scheduler.buildShutdownTask(() -> {
            Main.logger.info("Shutting down...");
        });

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
           final Player player = event.getPlayer();
           event.setSpawningInstance(instanceContainer);
           player.setRespawnPoint(new Pos(0,3,0));
        });
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            event.setCancelled(true);
        });

        minecraftServer.start("0.0.0.0", 25566);
    }
}