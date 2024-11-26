package rip.snicon.compass.update.sidebar;

import com.github.sniconmc.sidebar.SidebarManager;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import redis.clients.jedis.Jedis;
import rip.snicon.compass.Main;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UpdateSidebar {

    private static final String REDIS_ADDRESS = System.getenv("REDIS_ADDRESS") != null
            ? System.getenv("REDIS_ADDRESS")
            : "localhost";

    private static final int REDIS_PORT = 6379;
    private static final String PLAYER_COUNT_KEY = "network:playercount";
    private static final String PROXY_KEY_PATTERN = "proxy-*"; // Pattern to match proxy keys

    public static void startPlayerCountUpdater() {
        // Schedule task to run every 3 seconds (60 ticks = 3s at 20 TPS)
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            try (Jedis jedis = new Jedis(REDIS_ADDRESS, REDIS_PORT)) {
                // Fetch global player count
                String playerCount = jedis.get(PLAYER_COUNT_KEY);
                if (playerCount == null) {
                    playerCount = "0"; // Default to "0" if Redis key is not set
                }

                // Fetch proxy-specific player counts
                Map<String, String> proxyCounts = new HashMap<>();
                Set<String> keys = jedis.keys(PROXY_KEY_PATTERN); // Get all keys matching pattern
                for (String key : keys) {
                    String proxyName = key.replace("proxy-", ""); // Extract proxy name
                    String proxyCount = jedis.get(key);
                    if (proxyCount == null) {
                        proxyCount = "0"; // Default to "0" if Redis key is not set
                    }
                    proxyCounts.put(proxyName, proxyCount);
                }

                // Update placeholders for global and proxy-specific counts
                for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    // Update global placeholder
                    PlaceholderManager.setPlaceholderToPlayer(player, "online_network", playerCount);

                    // Update proxy-specific placeholders
                    for (Map.Entry<String, String> entry : proxyCounts.entrySet()) {
                        PlaceholderManager.setPlaceholderToPlayer(player, entry.getKey(), entry.getValue());
                    }
                }

                // Reload sidebars to reflect the updated placeholders
                SidebarManager.reloadSidebars();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).repeat(Duration.ofMillis(3000)).schedule();
    }
}
