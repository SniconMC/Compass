package rip.snicon.compass.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.TaskSchedule;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.sidebar.ShadowSidebar;


import java.util.UUID;

import static rip.snicon.compass.player.ShadowPlayerUtility.startOfflineAnimation;

public class ShadowPlayer extends Player {

    // Default
    private PlayerRank rank = PlayerRank.VILLAGER;
    private PlayerProfession profession = PlayerProfession.NITWIT;

    private double emeralds = 0;
    private double profession_xp = 0;
    private double achievement_points = 0;

    private ShadowSidebar viewingSidebar;
    private Boolean offlineMode = false;
    // Reference to your database manager
    private static final MongoDatabaseManager databaseManager = new MongoDatabaseManager();

    public ShadowPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        initializePlayerData();
    }

    public ShadowPlayer(@NotNull Player player) {
        this(player.getUuid(), player.getUsername(), player.getPlayerConnection());
    }

    private void initializePlayerData() {
        if (!databaseManager.isConnected()) {
            handleOfflineMode();
            return;
        }
        this.offlineMode = false;
        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        databaseManager.fetch("players", "uuid", getUuid().toString())
                .thenAccept(document -> {
                    if (document != null) {
                        this.rank = PlayerRank.valueOf(document.getString("rank"));
                        this.profession = PlayerProfession.valueOf(document.getString("profession"));
                        this.emeralds = document.getDouble("emeralds");
                        this.profession_xp = document.getDouble("profession_xp");
                        this.achievement_points = document.getDouble("achievement_points");
                    } else {
                        saveDataToDatabase(); // If no document found, save default values
                    }
                })
                .exceptionally(throwable -> {
                    handleOfflineMode();
                    startOfflineAnimation(this);
                    return null;
                });
    }

    private void saveDataToDatabase() {
        Document data = new Document("uuid", getUuid().toString())
                .append("rank", rank.name())
                .append("profession", profession.name())
                .append("emeralds", emeralds)
                .append("profession_xp", profession_xp)
                .append("achievement_points", achievement_points);

        databaseManager.save("players", "uuid", data)
                .exceptionally(throwable -> {
                    System.err.println("Failed to save player data: " + throwable.getMessage());
                    handleOfflineMode();
                    startOfflineAnimation(this);
                    return null;
                });
    }

    private void handleOfflineMode() {
        if (!isOfflineMode()) {
            this.offlineMode = true;
        }
    }

    public void setViewingSidebar(ShadowSidebar sidebar) {
        this.viewingSidebar = sidebar;
    }

    public boolean isOfflineMode(){
        return this.offlineMode;
    }
}
