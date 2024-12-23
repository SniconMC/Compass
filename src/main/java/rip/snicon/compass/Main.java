package rip.snicon.compass;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.compass.chat.ChatFormatter;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.database.redisdb.RedisCacheManager;
import rip.snicon.compass.instances.MysteryInstance;
import rip.snicon.compass.listeners.Global;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.sidebar.MysterySidebar;


public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        MongoDatabaseManager.connect("mongodb://localhost:27017", "minestom");

        RedisCacheManager.initialize("localhost", 6379);

        MysterySidebar.start();

        ChatFormatter.setup();

        MojangAuth.init();

        MinecraftServer.getConnectionManager().setPlayerProvider(MysteryPlayer::new);

        // Listener
        new Global();

        // Start the server
        int port = Integer.parseInt(System.getenv().getOrDefault("SERVER_PORT", "25565"));
        minecraftServer.start("0.0.0.0", port);
        Main.logger.info("Server starting on port: {}", port);
    }
}
