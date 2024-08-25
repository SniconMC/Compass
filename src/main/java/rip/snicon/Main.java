package rip.snicon;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.blockhandlers.SkullHandler;
import rip.snicon.commands.ReloadCommand;
import rip.snicon.commands.world.TravelCommand;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.listeners.Global;
import rip.snicon.modules.container.ContainerCreator;
import rip.snicon.modules.container.HotbarCreator;
import rip.snicon.modules.oblivion.OblivionCreator;
import rip.snicon.modules.sidebar.SidebarCreator;
import rip.snicon.utils.motd.MOTD;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Initialize the Instance creator
        InstanceCreator instanceCreator = new InstanceCreator();
        SidebarCreator sidebarCreator = new SidebarCreator();
        HotbarCreator hotbarCreator = new HotbarCreator();
        ContainerCreator containerCreator = new ContainerCreator();
        OblivionCreator oblivionCreator = new OblivionCreator();
        MOTD motd = new MOTD();
        Global globalListener = new Global();

        MinecraftServer.getCommandManager().register(new TravelCommand());
        MinecraftServer.getCommandManager().register(new ReloadCommand());
        MinecraftServer.getBlockManager().registerHandler("minecraft:skull", SkullHandler::new);
        // Start the server
        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);

    }
}