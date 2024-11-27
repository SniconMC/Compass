package rip.snicon.compass.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import rip.snicon.compass.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShadowPlayerUtility {

    private static Map<UUID, Task> animationTasks = new HashMap<>();

    public static void startOfflineAnimation(ShadowPlayer player) {
        String[] animations = {
                "<red>→ </red><gray>Database connection lost!</gray><red> ←</red>",
                "<red>→  </red><gray>Database connection lost!</gray><red>  ←</red>",
                "<red>→   </red><gray>Database connection lost!</gray><red>   ←</red>",
                "<red>→  </red><gray>Database connection lost!</gray><red>  ←</red>"
        };


        final Component mainTitle = TextUtils.convertStringToComponent("<gold>⚠</gold> <red>You are playing in Offline Mode!</red> <gold>⚠</gold>");
        final Component subtitle = TextUtils.convertStringToComponent("<gray>Any progress you make will <underlined><bold>not</bold></underlined> be saved!</gray>");

        final Title title = Title.title(mainTitle, subtitle);

        player.showTitle(title);


        // Cancel existing task if there is one for this player
        cancelExistingAnimationTask(player);

        Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!player.isOnline()) {
                // If the player disconnects, stop the animation
                return;
            }

            if (!player.isOfflineMode()) {
                // If the player goes online, display a final message and stop the animation
                player.sendActionBar(TextUtils.convertStringToComponent("<green>You are online.</green>"));
                cancelExistingAnimationTask(player); // Cancel task when the player is online
                return;
            }

            // Safely calculate the step index (ensuring it's within bounds)
            int step = (int) ((System.currentTimeMillis() / 500) % animations.length);
            if (step < 0) step = 0;  // Ensure step is always non-negative

            // Update the animation
            player.sendActionBar(TextUtils.convertStringToComponent(animations[step]));
        }).repeat(TaskSchedule.millis(500)).schedule();

        // Store the task for future cancellation
        animationTasks.put(player.getUuid(), task);
    }

    private static void cancelExistingAnimationTask(ShadowPlayer player) {
        Task existingTask = animationTasks.get(player.getUuid());
        if (existingTask != null) {
            existingTask.cancel();
            animationTasks.remove(player.getUuid());
        }
    }
}
