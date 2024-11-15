package rip.snicon.compass;

import com.github.sniconmc.container.ContainerMain;
import com.github.sniconmc.gandalf.GandalfMain;
import com.github.sniconmc.momentum.MomentumMain;
import com.github.sniconmc.oblivion.OblivionMain;
import com.github.sniconmc.sidebar.SidebarMain;
import com.github.sniconmc.utils.UtilsMain;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.timer.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.compass.blockhandlers.SignHandler;
import rip.snicon.compass.blockhandlers.SkullHandler;
import rip.snicon.compass.commands.admin.world.TravelCommand;
import rip.snicon.compass.commands.player.HubCommand;
import rip.snicon.compass.instances.InstanceCreator;
import rip.snicon.compass.listeners.Global;
import rip.snicon.compass.proxy.Servers;
import rip.snicon.compass.utils.motd.MOTD;

import java.util.UUID;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();
        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();

        InstanceCreator instanceCreator = InstanceCreator.getInstance();
        

        // Initialize MOTD
        MOTD motd = new MOTD();

        // Set global listener
        Global globalListener = new Global();

        // Initialize dependencies
        UtilsMain.init();
        SidebarMain.init();
        MomentumMain.init();
        ContainerMain.init();
        OblivionMain.init();
        GandalfMain.init();



        // Register commands
        MinecraftServer.getCommandManager().register(new TravelCommand());
        MinecraftServer.getCommandManager().register(new HubCommand());

        MinecraftServer.getBlockManager().registerHandler(SkullHandler.KEY, SkullHandler::new);
        MinecraftServer.getBlockManager().registerHandler(SignHandler.KEY, SignHandler::new);

        scheduler.buildShutdownTask(() -> {
            Servers.unregister(); // Deregisters the server
            Main.logger.info("Shutting down...");
        });


        // Default port to 25565 if no environment variable is set
        int port = Integer.parseInt(System.getenv().getOrDefault("SERVER_PORT", "25565"));
        Main.logger.info("Server port: {}", port);
        // Default port to 25565 if no environment variable is set
        String velocitySecret = System.getenv().getOrDefault("VELOCITY_SECRET", "balle123");
        VelocityProxy.enable(velocitySecret);
        // Start the server
        minecraftServer.start("0.0.0.0", port);
        Servers.register();   // Registers the server

    }
}