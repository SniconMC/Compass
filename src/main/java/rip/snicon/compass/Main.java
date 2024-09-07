package rip.snicon.compass;

import com.github.sniconmc.container.ContainerMain;
import com.github.sniconmc.momentum.MomentumMain;
import com.github.sniconmc.oblivion.OblivionMain;
import com.github.sniconmc.sidebar.SidebarMain;
import com.github.sniconmc.utils.UtilsMain;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.timer.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.compass.blockhandlers.SignHandler;
import rip.snicon.compass.blockhandlers.SkullHandler;
import rip.snicon.compass.commands.admin.world.TravelCommand;
import rip.snicon.compass.commands.player.HubCommand;
import rip.snicon.compass.gandalf.GandalfMain;
import rip.snicon.compass.instances.InstanceCreator;
import rip.snicon.compass.listeners.Global;
import rip.snicon.compass.utils.motd.MOTD;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();
        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();

        InstanceCreator instanceCreator = new InstanceCreator();

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
            Main.logger.info("Shutting down...");
        });


        // Start the server
        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);

    }
}