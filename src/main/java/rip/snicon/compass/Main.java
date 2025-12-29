package rip.snicon.compass;

import net.minestom.server.MinecraftServer;
import nub.wi1helm.smoxy.SMoxy;
import nub.wi1helm.smoxy.SMoxyConfig;
import nub.wi1helm.smoxy.SMoxyMode;
import nub.wi1helm.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.compass.chat.ChatFormatter;
import rip.snicon.compass.listeners.Global;
import rip.snicon.compass.npc.NPC;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.sidebar.MysterySidebar;
import rip.snicon.compass.utils.blockhandlers.SignHandler;
import rip.snicon.compass.utils.blockhandlers.SkullHandler;



public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static boolean joinable = true;

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Initialize general-purpose features
        MysterySidebar.create();
        ChatFormatter.setup();
        NPC.initializeAll();
        Template.init();
        SMoxy.init(SMoxyMode.STANDALONE);
        SMoxy.setupMongoDB();

        MinecraftServer.getBlockManager().registerHandler(SkullHandler.KEY, SkullHandler::new);
        MinecraftServer.getBlockManager().registerHandler(SignHandler.KEY, SignHandler::new);

        // Set player provider
        MinecraftServer.getConnectionManager().setPlayerProvider(MysteryPlayer::new);
        MinecraftServer.setCompressionThreshold(0);

        // Set global listeners
        new Global();

        // Start the server
        minecraftServer.start(SMoxyConfig.SERVER_IP, SMoxyConfig.SERVER_PORT);
        Main.logger.info("Server '{}' running on {}", SMoxy.serverName, SMoxy.getServerAddress());
    }




}
