package rip.snicon.compass.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.sidebar.ShadowSidebar;
import rip.snicon.compass.utils.TextUtils;

import java.util.UUID;

public class ShadowPlayer extends Player {

    // Default
    private PlayerRank rank = PlayerRank.VILLAGER;
    private PlayerProfession profession = PlayerProfession.NITWIT;

    private double emeralds = 0;
    private double profession_xp = 0;
    private double achivement_points = 0;

    private ShadowSidebar viewingSidebar;

    public ShadowPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);


    }
    public ShadowPlayer(@NotNull Player player) {
        this(player.getUuid(), player.getUsername(), player.getPlayerConnection());
    }

    public void setViewingSidebar(ShadowSidebar sidebar) {
        this.viewingSidebar = sidebar;
    }
}
