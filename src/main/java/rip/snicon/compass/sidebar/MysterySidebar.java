package rip.snicon.compass.sidebar;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.TaskSchedule;
import rip.snicon.compass.Main;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MysterySidebar {

    private static final Map<UUID, Sidebar> sidebarCache = new HashMap<>();
    private static int animationCounter = 0;

    public static void start() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            animationCounter = (animationCounter + 1) % 80; // Cycle animation counter (0–50)

            for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (p instanceof MysteryPlayer player) {
                    // Get or create the sidebar for the player
                    Sidebar sidebar = sidebarCache.get(player.getUuid());

                    if (sidebar == null) {
                        // Create new sidebar if it doesn't exist
                        sidebar = new Sidebar(TextUtils.convertStringToComponent(getSidebarTitle(animationCounter)));

                        // Add hardcoded lines
                        sidebar.createLine(new Sidebar.ScoreboardLine("date", TextUtils.convertStringToComponent("<gray>" + new SimpleDateFormat("MM/dd/yy").format(new Date())), 8, Sidebar.NumberFormat.blank()));
                        sidebar.createLine(new Sidebar.ScoreboardLine("space1", TextUtils.convertStringToComponent("<gray> "), 7, Sidebar.NumberFormat.blank()));
                        sidebar.createLine(new Sidebar.ScoreboardLine("line1", TextUtils.convertStringToComponent("<white>Welcome to Multiverse Mystery!"), 6, Sidebar.NumberFormat.blank()));
                        sidebar.createLine(new Sidebar.ScoreboardLine("space2", TextUtils.convertStringToComponent("<gray> "), 5, Sidebar.NumberFormat.blank()));

                        // Add dynamic player-specific stats
                        sidebar.createLine(new Sidebar.ScoreboardLine("emeralds", TextUtils.convertStringToComponent("<white>Emeralds: <green>" + player.getDataHandler().getEmeralds()), 4, Sidebar.NumberFormat.blank()));
                        sidebar.createLine(new Sidebar.ScoreboardLine("xp", TextUtils.convertStringToComponent("<white>XP: <gold>" + player.getDataHandler().getProfessionXp()), 3, Sidebar.NumberFormat.blank()));

                        // Add end ip
                        sidebar.createLine(new Sidebar.ScoreboardLine("space3", TextUtils.convertStringToComponent("<gray> "), 1, Sidebar.NumberFormat.blank()));
                        sidebar.createLine(new Sidebar.ScoreboardLine("ip", TextUtils.convertStringToComponent("<yellow>www.fuckyouhängdigsjälv.net"), 0, Sidebar.NumberFormat.blank()));

                        // Attach the sidebar to the player
                        sidebar.addViewer(player);

                        // Cache the sidebar for future updates
                        sidebarCache.put(player.getUuid(), sidebar);
                    } else {
                        // Update the title of the existing sidebar
                        sidebar.setTitle(TextUtils.convertStringToComponent(getSidebarTitle(animationCounter)));

                        // Update dynamic player-specific stats
                        sidebar.updateLineContent("emeralds", TextUtils.convertStringToComponent("<white>Emeralds: <green>" + player.getDataHandler().getEmeralds()));
                        sidebar.updateLineContent("xp", TextUtils.convertStringToComponent("<white>XP: <gold>" + player.getDataHandler().getProfessionXp()));
                        sidebar.updateLineContent("achievements", TextUtils.convertStringToComponent("<white>Achievements: <aqua>" + player.getDataHandler().getAchievementPoints()));

                    }
                }
            }
            return TaskSchedule.tick(2); // Run every 2 ticks
        });
    }

    private static String getSidebarTitle(int counter) {
        String baseText = "MULTIVERSE MYSTERY";
        String[] colors = {"<light_purple><bold>", "<dark_purple><bold>"};
        String endColor = "<green><bold>";
        String endText = "";

        int animationSpeed = 40; // Frame speed for color change

        // Calculate which color step to use based on the animation counter
        int colorIndex = (counter / animationSpeed) % colors.length;

        // Handle the transition effect (pulsing gray)
        if (counter % animationSpeed == 0) {
            // Gray and non-bold transition effect for a frame
            return "<gray>" + baseText + "<reset>" + endColor + endText;
        }

        // Return the color based on the current animation step
        return colors[colorIndex] + baseText + endColor + endText;
    }

    public static Map<UUID, Sidebar> getSidebarCache(){
        return sidebarCache;
    }
}
