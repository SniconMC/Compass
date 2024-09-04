package rip.snicon;

import com.github.sniconmc.container.ContainerMain;
import com.github.sniconmc.momentum.MomentumMain;
import com.github.sniconmc.sidebar.SidebarMain;
import com.github.sniconmc.utils.UtilsMain;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.timer.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.blockhandlers.SkullHandler;
import rip.snicon.commands.admin.world.TravelCommand;
import rip.snicon.commands.player.HubCommand;
import rip.snicon.gandalf.GandalfManager;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.listeners.Global;
import rip.snicon.oblivion.OblivionMain;
import rip.snicon.utils.motd.MOTD;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();
        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();

        InstanceCreator instanceCreator = new InstanceCreator();

        GandalfManager gandalfManager = new GandalfManager();
        // Initialize MOTD
        MOTD motd = new MOTD();

        // Initialize dependencies
        UtilsMain.init();
        SidebarMain.init();
        MomentumMain.init();
        ContainerMain.init();
        OblivionMain.init();

        InteractListener.onYourMother();


        // Set global listener
        Global globalListener = new Global();

        // Register commands
        MinecraftServer.getCommandManager().register(new TravelCommand());
        MinecraftServer.getCommandManager().register(new HubCommand());

        MinecraftServer.getBlockManager().registerHandler("minecraft:skull", SkullHandler::new);

        scheduler.buildShutdownTask(() -> {
            Main.logger.info("Shutting down...");
        });


        // Start the server
        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);

    }
}