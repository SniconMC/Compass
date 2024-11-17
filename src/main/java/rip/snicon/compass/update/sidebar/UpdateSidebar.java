package rip.snicon.compass.update.sidebar;

import com.github.sniconmc.sidebar.SidebarManager;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import redis.clients.jedis.Jedis;

import java.time.Duration;

public class UpdateSidebar {

    private static final String REDIS_ADDRESS = System.getenv("REDIS_ADDRESS") != null
            ? System.getenv("REDIS_ADDRESS")
            : "localhost";

    private static final int REDIS_PORT = 6379;
    private static final String PLAYER_COUNT_KEY = "network:playercount";

    public static void startPlayerCountUpdater() {
        // Schedule task to run every 5 seconds (100 ticks = 5s at 20 TPS)
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            try (Jedis jedis = new Jedis(REDIS_ADDRESS, REDIS_PORT)) {
                // Fetch player count from Redis
                String playerCount = jedis.get(PLAYER_COUNT_KEY);
                if (playerCount == null) {
                    playerCount = "0"; // Default to "0" if Redis key is not set
                }

                // Update placeholder for all players
                for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    PlaceholderManager.setPlaceholderToPlayer(player, "online_network", playerCount);
                }

                // Reload sidebars to reflect the updated placeholder
                SidebarManager.reloadSidebars();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).repeat(Duration.ofMillis(3000)).schedule();
    }


}
