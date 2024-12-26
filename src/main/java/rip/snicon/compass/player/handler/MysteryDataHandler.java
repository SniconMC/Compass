package rip.snicon.compass.player.handler;

import net.minestom.server.MinecraftServer;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.PlayerRank;
import rip.snicon.compass.player.profession.PlayerProfession;
import rip.snicon.compass.utils.TextUtils;

import java.util.UUID;

public class MysteryDataHandler {

    private final UUID uuid;
    private PlayerRank rank = PlayerRank.VILLAGER;
    private PlayerProfession profession = PlayerProfession.NITWIT;
    private double emeralds = 0;
    private double professionXp = 0;
    private double achievementPoints = 0;

    public MysteryDataHandler(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public void fetchDataFromDatabase() {
        MongoDatabaseManager.fetch("players", "uuid", uuid.toString()).thenAccept(document -> {
            if (document != null) {
                loadPlayerData(document);
            } else {
                saveDataToDatabase();
            }
        });
    }

    public void saveDataToDatabase() {
        Document data = new Document("uuid", uuid.toString())
                .append("rank", rank.name())
                .append("profession", profession.name())
                .append("emeralds", emeralds)
                .append("profession_xp", professionXp)
                .append("achievement_points", achievementPoints);

        MongoDatabaseManager.save("players", "uuid", data).exceptionally(throwable -> {
            System.err.println("Failed to save player data: " + throwable.getMessage());
            return null;
        });
    }

    private void loadPlayerData(Document document) {
        this.rank = PlayerRank.valueOf(document.getString("rank"));
        this.profession = PlayerProfession.valueOf(document.getString("profession"));
        this.emeralds = document.getDouble("emeralds");
        this.professionXp = document.getDouble("profession_xp");
        this.achievementPoints = document.getDouble("achievement_points");
    }

    // Getters and setters for rank, profession, emeralds, etc.
    public PlayerRank getRank() {
        return rank;
    }

    public void updateRank(PlayerRank newRank, boolean sendMessage) {
        this.rank = newRank;
        if (sendMessage) {
            sendPlayerMessage(String.format("Your rank has been updated to %s.", rank.name()));
        }
        saveDataToDatabase();
    }

    public double getEmeralds() {
        return emeralds;
    }

    public void updateEmeralds(double amount, boolean sendMessage) {
        this.emeralds += amount;
        if (sendMessage) {
            sendPlayerMessage(String.format("You now have %.1f emeralds.", emeralds));
        }
        saveDataToDatabase();
    }

    public PlayerProfession getProfession() {
        return profession;
    }

    public void updateProfession(PlayerProfession newProfession, boolean sendMessage) {
        this.profession = newProfession;
        if (sendMessage) {
            sendPlayerMessage(String.format("Your profession has been updated to %s.", profession.name()));
        }
        saveDataToDatabase();
    }

    public double getProfessionXp() {
        return professionXp;
    }

    public void updateProfessionXp(double amount, boolean sendMessage) {
        this.professionXp += amount;
        if (sendMessage) {
            sendPlayerMessage(String.format("You gained %.1f XP towards your profession.", amount));
        }
        checkForProfessionLevelUp();
        saveDataToDatabase();
    }

    public double getAchievementPoints() {
        return achievementPoints;
    }

    public void updateAchievementPoints(double amount, boolean sendMessage) {
        this.achievementPoints += amount;
        if (sendMessage) {
            sendPlayerMessage(String.format("You now have %.1f achievement points.", achievementPoints));
        }
        saveDataToDatabase();
    }

    private void checkForProfessionLevelUp() {
        double totalXp = getProfessionXp();
        PlayerProfession currentProfession = getProfession();
        PlayerProfession[] professions = PlayerProfession.values();

        // Find the current profession index
        int currentIndex = -1;
        for (int i = 0; i < professions.length; i++) {
            if (professions[i] == currentProfession) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) {
            return; // Invalid current profession
        }

        double cumulativeXp = 0;
        PlayerProfession leveledProfession = null;

        // Start from the next profession after the current one
        for (int i = currentIndex + 1; i < professions.length; i++) {
            cumulativeXp += professions[i].getReqXP();
            if (totalXp >= cumulativeXp) {
                leveledProfession = professions[i];
            } else {
                break;
            }
        }

        // If a level-up occurred
        if (leveledProfession != null && leveledProfession != currentProfession) {
            updateProfession(leveledProfession, true);
            sendPlayerMessage(String.format(
                    "Congratulations! You leveled up from %s to %s.",
                    currentProfession.name(),
                    leveledProfession.name()
            ));
        }
    }

    private void sendPlayerMessage(String message) {
        MysteryPlayer player = MysteryPlayer.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(TextUtils.convertStringToComponent(message));
        }
    }
}
