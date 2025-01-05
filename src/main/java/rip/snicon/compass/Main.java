package rip.snicon.compass;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.compass.chat.ChatFormatter;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.database.redisdb.RedisCacheManager;
import rip.snicon.compass.listeners.Global;
import rip.snicon.compass.npc.MysteryNPC;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.ServerRegistry;
import rip.snicon.compass.sidebar.MysterySidebar;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Setup database connections
        setupDatabases();

        // Initialize general-purpose features
        MysterySidebar.create();
        MysteryNPC.create();
        ChatFormatter.setup();
        MojangAuth.init();

        // Set player provider
        MinecraftServer.getConnectionManager().setPlayerProvider(MysteryPlayer::new);

        // Set global listeners
        new Global();

        // Environment variables
        String serverIp = System.getenv().getOrDefault("SERVER_IP", "0.0.0.0");
        String serverPort = System.getenv().getOrDefault("SERVER_PORT", "25565");
        String serverLabel = System.getenv().getOrDefault("SERVER_LABEL", "hub");

        // Register the server
        String serverId = registerServer(serverIp, serverPort, serverLabel);

        // Start the server
        minecraftServer.start("0.0.0.0", Integer.parseInt(serverPort));
        Main.logger.info("Server {} ({}) running on {}:{}", serverId, serverLabel, serverIp, serverPort);

        // Add shutdown hook for cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ServerRegistry.removeServer(serverId);
            RedisCacheManager.shutdown();
            MinecraftServer.stopCleanly();
        }));
    }

    private static void setupDatabases() {
        MongoDatabaseManager.connect("mongodb://localhost:27017", "minestom");
        RedisCacheManager.initialize("localhost", 6379);
    }

    private static String registerServer(String ip, String port, String label) {
        Map<String, String> properties = new HashMap<>();
        properties.put("ip", ip);
        properties.put("port", port);
        properties.put("label", label);
        properties.put("state", "AVAILABLE");
        properties.put("currentPlayers", "0");
        properties.put("maxPlayers", "100"); // Adjust max players as needed
        String serverId = ServerRegistry.generateServerId(label);
        ServerRegistry.registerServer(serverId, label, properties);
        return serverId;
    }
}
