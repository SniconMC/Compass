package rip.snicon;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.blockhandlers.SkullHandler;
import rip.snicon.commands.world.TravelWorldCommand;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.listeners.Global;
import rip.snicon.modules.sidebar.SidebarCreator;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Initialize the Instance creator
        InstanceCreator instanceCreator = new InstanceCreator();
        SidebarCreator sidebarCreator = new SidebarCreator();

        Global globalListener = new Global();

        MinecraftServer.getCommandManager().register(new TravelWorldCommand());

        MinecraftServer.getBlockManager().registerHandler("minecraft:skull", SkullHandler::new);
        // Start the server
        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);

    }
}