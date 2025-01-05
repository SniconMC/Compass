package rip.snicon.compass;

import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.common.TransferPacket;
import org.json.JSONObject;
import rip.snicon.compass.database.redisdb.RedisCacheManager;

import java.util.*;

public class ServerRegistry {

    private static final String REDIS_KEY_PREFIX = "server:";

    /**
     * Registers the server with Redis.
     * @param id The server's unique ID.
     * @param label The server's label (e.g., "hub", "minigame").
     * @param properties Additional server properties (e.g., "state", "currentPlayers").
     */
    public static void registerServer(String id, String label, Map<String, String> properties) {
        JSONObject json = new JSONObject(properties);
        json.put("id", id);
        json.put("label", label);
        RedisCacheManager.save(REDIS_KEY_PREFIX + id, json.toString());
    }

    /**
     * Gets all servers with the given label.
     * @param label The label to filter servers by.
     * @return A list of server properties as JSON objects.
     */
    public static List<JSONObject> getServersByLabel(String label) {
        List<JSONObject> servers = new ArrayList<>();
        Set<String> keys = RedisCacheManager.getKeys(REDIS_KEY_PREFIX + "*");

        for (String key : keys) {
            String jsonString = RedisCacheManager.fetch(key);
            if (jsonString != null) {
                JSONObject server = new JSONObject(jsonString);
                if (label.equals(server.optString("label"))) {
                    servers.add(server);
                }
            }
        }
        return servers;
    }

    /**
     * Gets the best available server by label.
     * @param label The label to filter servers by.
     * @return The properties of the best available server as a JSONObject.
     */
    public static Optional<JSONObject> getBestServer(String label) {
        List<JSONObject> servers = getServersByLabel(label);

        return servers.stream()
                .filter(server -> "AVAILABLE".equalsIgnoreCase(server.optString("state")))
                .min(Comparator.comparingInt(server -> server.optInt("currentPlayers", Integer.MAX_VALUE)));
    }

    /**
     * Removes a server from the registry.
     * @param id The server's unique ID.
     */
    public static void removeServer(String id) {
        RedisCacheManager.delete(REDIS_KEY_PREFIX + id);
    }

    /**
     * Generates a random unique ID for the server.
     * @param label The label for the server.
     * @return A random unique ID.
     */
    public static String generateServerId(String label) {
        return label + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Connects a player to the best available proxy based on label.
     * @param player The player to connect.
     * @param label The label of the target proxy (e.g., "hub", "minigame").
     */
    public static void connectPlayerToBestServer(Player player, String label) {
        Optional<JSONObject> bestServer = getBestServer(label);

        if (bestServer.isPresent()) {
            JSONObject server = bestServer.get();
            String ip = server.optString("ip");
            int port = server.optInt("port");

            Main.logger.info("Connecting player " + player.getUsername() + " to server: " + ip + ":" + port);
            connectPlayer(player, ip, port);
        } else {
            player.sendMessage("No available servers found for label: " + label);
        }
    }

    /**
     * Sends a player to a specific server using TransferPacket.
     * @param player The player to connect.
     * @param ip The IP address of the target server.
     * @param port The port of the target server.
     */
    public static void connectPlayer(Player player, String ip, int port) {
        Main.logger.info("Sending player " + player.getUsername() + " to " + ip + ":" + port);
        player.sendPacket(new TransferPacket(ip, port));
    }
}
