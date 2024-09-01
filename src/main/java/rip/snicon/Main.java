package rip.snicon;

import com.github.sniconmc.momentum.MomentumMain;
import com.github.sniconmc.utils.UtilsMain;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.timer.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.blockhandlers.SkullHandler;
import rip.snicon.commands.admin.ReloadCommand;
import rip.snicon.commands.admin.world.TravelCommand;
import rip.snicon.commands.player.HubCommand;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.listeners.Global;
import rip.snicon.modules.container.ContainerCreator;
import rip.snicon.modules.container.HotbarCreator;
import rip.snicon.modules.momentum.MomentumManager;
import rip.snicon.modules.oblivion.OblivionCreator;
import rip.snicon.modules.sidebar.SidebarCreator;
import rip.snicon.utils.motd.MOTD;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();
        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();

        // Initialize your mother
        InstanceCreator instanceCreator = new InstanceCreator();
        SidebarCreator sidebarCreator = new SidebarCreator();
        HotbarCreator hotbarCreator = new HotbarCreator();
        ContainerCreator containerCreator = new ContainerCreator();
        OblivionCreator oblivionCreator = new OblivionCreator();
        /*MomentumManager momentumManager = new MomentumManager();*/
        MOTD motd = new MOTD();

        UtilsMain.init();

        MomentumMain.init();

        Global globalListener = new Global();

        MinecraftServer.getCommandManager().register(new TravelCommand());
        MinecraftServer.getCommandManager().register(new ReloadCommand());
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