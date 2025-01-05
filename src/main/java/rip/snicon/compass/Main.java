package rip.snicon.compass;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.velocity.VelocityProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import rip.snicon.compass.chat.ChatFormatter;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.database.redisdb.RedisCacheManager;
import rip.snicon.compass.listeners.Global;
import rip.snicon.compass.npc.MysteryNPC;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.sidebar.MysterySidebar;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static String serverName; // Store the server's name
    private static String proxyAddress; // Proxy address

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Environment variables
        String serverIp = System.getenv().getOrDefault("SERVER_IP", "0.0.0.0");
        String serverPort = System.getenv().getOrDefault("SERVER_PORT", "25566");
        String serverLabel = System.getenv().getOrDefault("SERVER_LABEL", "hub");
        String redisAddress = System.getenv().getOrDefault("REDIS_ADDR", "localhost:6379");
        String redisPassword = System.getenv("REDIS_PASSWORD");
        String velocitySecret = System.getenv().getOrDefault("VELOCITY_SECRET", "balle123");
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", "mongodb://localhost:27017");
        String mongoDbName = System.getenv().getOrDefault("MONGO_DB_NAME", "minestom");

        // Setup databases
        setupDatabases(mongoUri, mongoDbName, redisAddress, redisPassword);

        // Initialize general-purpose features
        MysterySidebar.create();
        MysteryNPC.create();
        ChatFormatter.setup();

        // Set player provider
        MinecraftServer.getConnectionManager().setPlayerProvider(MysteryPlayer::new);

        // Set global listeners
        new Global();

        // Generate the server name using label + cropped UUID
        serverName = generateServerName(serverLabel);

        if (serverName == null) {
            logger.error("Failed to generate the server name. Shutting down.");
            System.exit(1);
        }

        // Start checking for matching proxy
        startProxyCheckTask(serverLabel, serverIp, serverPort);

        // Start the server
        VelocityProxy.enable(velocitySecret);
        minecraftServer.start(serverIp, Integer.parseInt(serverPort));
        Main.logger.info("Server '{}' running on {}:{}", serverName, serverIp, serverPort);

        // Add shutdown hook for cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (proxyAddress != null) {
                ServerRegistry.unregisterServer(serverName);
            }
            RedisCacheManager.shutdown();
            MinecraftServer.stopCleanly();
        }));
    }

    private static void setupDatabases(String mongoUri, String mongoDbName, String redisAddress, String redisPassword) {
        try {
            // Setup MongoDB
            MongoDatabaseManager.connect(mongoUri, mongoDbName);
            logger.info("Connected to MongoDB at {}", mongoUri);

            // Parse Redis address
            String[] redisParts = redisAddress.split(":");
            if (redisParts.length != 2) {
                throw new IllegalArgumentException("Invalid Redis address format. Expected 'host:port'.");
            }
            String redisHost = redisParts[0];
            int redisPort = Integer.parseInt(redisParts[1]);

            // Setup Redis
            RedisCacheManager.initialize(redisHost, redisPort);
            logger.info("Connected to Redis at {}:{}", redisHost, redisPort);
        } catch (Exception e) {
            logger.error("Failed to initialize databases: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    private static void startProxyCheckTask(String serverLabel, String serverIp, String serverPort) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    String proxyKey = "proxy:" + serverLabel;
                    Map<String, String> proxyInfo = RedisCacheManager.fetchMap(proxyKey); // Fetch all fields in a hash key

                    if (proxyInfo != null && !proxyInfo.isEmpty()) {
                        proxyAddress = proxyInfo.get("address");
                        String proxyPort = proxyInfo.get("port");

                        if (proxyAddress != null && proxyPort != null) {
                            String fullProxyAddress = proxyAddress + ":" + proxyPort;
                            ServerRegistry.registerServer(serverName, fullProxyAddress);
                            logger.info("Registered server '{}' to proxy '{}:{}'", serverName, proxyAddress, proxyPort);
                            cancel(); // Stop the task after successful registration
                        }
                    } else {
                        logger.info("No matching proxy found for label '{}'. Retrying in 10 seconds...", serverLabel);
                    }
                } catch (Exception e) {
                    logger.error("Error while checking for proxy: {}", e.getMessage(), e);
                }
            }
        }, 0, 10000); // Run every 10 seconds
    }


    private static String generateServerName(String label) {
        try {
            // Combine label with cropped UUID
            String croppedUUID = java.util.UUID.randomUUID().toString().substring(0, 8);
            String generatedName = label + "-" + croppedUUID;
            logger.info("Generated server name: {}", generatedName);
            return generatedName;
        } catch (Exception e) {
            logger.error("Error generating the server name: {}", e.getMessage(), e);
            return null;
        }
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getProxyAddress() {
        return proxyAddress;
    }
}
