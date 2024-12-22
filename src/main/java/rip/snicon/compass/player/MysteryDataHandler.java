package rip.snicon.compass.player;

import org.bson.Document;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.inventory.MysteryInventory;
import rip.snicon.compass.inventory.MysteryInventoryType;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.profession.PlayerProfession;
import rip.snicon.compass.player.settings.PlayerSetting;
import rip.snicon.compass.instances.regions.MysteryRegion;

import java.util.*;

public class MysteryDataHandler {

    private static final MongoDatabaseManager databaseManager = new MongoDatabaseManager();
    private static final Map<UUID, MysteryDataHandler> userCache = new HashMap<>();

    private final UUID uuid;
    private PlayerRank rank = PlayerRank.VILLAGER;
    private PlayerProfession profession = PlayerProfession.NITWIT;
    private double emeralds = 0;
    private double professionXp = 90;
    private double achievementPoints = 0;

    private final Map<Integer, MysteryItemType> inventory = new HashMap<>(MysteryInventoryType.DEFAULT.getStaticInventory().getItems());
    private final Map<PlayerSetting, Boolean> settings = new EnumMap<>(PlayerSetting.class);
    private final Set<String> discoveredRegions = new HashSet<>();

    private MysteryDataHandler(UUID uuid) {
        this.uuid = uuid;
        loadDefaultSettings();
    }

    public static MysteryDataHandler getUser(UUID uuid) {
        return userCache.computeIfAbsent(uuid, MysteryDataHandler::new);
    }

    public void fetchDataFromDatabase() {
        databaseManager.fetch("players", "uuid", uuid.toString())
                .thenAccept(document -> {
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

        // Save settings as a sub-document
        Document settingsDocument = new Document();
        settings.forEach((key, value) -> settingsDocument.append(key.getKey(), value));
        data.append("settings", settingsDocument);

        // Save discovered regions as a list
        data.append("discovered_regions", new ArrayList<>(discoveredRegions));

        // Save inventory as a sub-document
        Document inventoryDocument = new Document();
        inventory.forEach((slot, itemType) -> inventoryDocument.append(String.valueOf(slot), itemType.name()));
        data.append("inventory", inventoryDocument);

        // Save to the database
        databaseManager.save("players", "uuid", data).exceptionally(throwable -> {
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

        // Load settings
        if (document.containsKey("settings")) {
            Document settingsDocument = document.get("settings", Document.class);
            for (PlayerSetting setting : PlayerSetting.values()) {
                if (settingsDocument.containsKey(setting.getKey())) {
                    settings.put(setting, settingsDocument.getBoolean(setting.getKey()));
                }
            }
        }

        // Load discovered regions
        if (document.containsKey("discovered_regions")) {
            List<String> regions = document.getList("discovered_regions", String.class);
            discoveredRegions.addAll(regions);
        }

        // Load inventory
        if (document.containsKey("inventory")) {
            Document inventoryDocument = document.get("inventory", Document.class);
            for (String slotKey : inventoryDocument.keySet()) {
                int slot = Integer.parseInt(slotKey);
                String itemTypeName = inventoryDocument.getString(slotKey);
                try {
                    MysteryItemType itemType = MysteryItemType.valueOf(itemTypeName);
                    inventory.put(slot, itemType);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid MysteryItemType in database: " + itemTypeName);
                }
            }
        }
    }


    public boolean hasDiscoveredRegion(MysteryRegion region) {
        return discoveredRegions.contains(region.name());
    }

    public void updateDiscoveredRegion(MysteryRegion region) {
        if (!discoveredRegions.contains(region.name())) {
            discoveredRegions.add(region.name());
            updateEmeralds(region.getEmeralds());
            updateProfessionXp(region.getXp());

            saveDataToDatabase();
        }
    }

    public void updateRank(PlayerRank newRank) {
        this.rank = newRank;
        saveDataToDatabase();
    }

    public PlayerRank getRank() {
        return this.rank;
    }

    public void updateProfession(PlayerProfession newProfession) {
        this.profession = newProfession;
        saveDataToDatabase();
    }

    public PlayerProfession getProfession() {
        return this.profession;
    }

    public void updateEmeralds(double amount) {
        this.emeralds = amount;
        saveDataToDatabase();
    }

    public double getEmeralds() {
        return this.emeralds;
    }

    public void updateProfessionXp(double amount) {
        this.professionXp = amount;
        saveDataToDatabase();
    }

    public double getProfessionXp() {
        return this.professionXp;
    }

    public void updateAchievementPoints(double amount) {
        this.achievementPoints = amount;
        saveDataToDatabase();
    }

    public double getAchievementPoints() {
        return this.achievementPoints;
    }

    private void loadDefaultSettings() {
        for (PlayerSetting setting : PlayerSetting.values()) {
            settings.put(setting, setting.getDefaultValue());
        }
    }

    public boolean getSetting(PlayerSetting setting) {
        return settings.getOrDefault(setting, setting.getDefaultValue());
    }

    public void updateSetting(PlayerSetting setting, boolean value) {
        settings.put(setting, value);
        saveDataToDatabase();
    }


    public void setInventoryItem(int slot, MysteryItemType itemType) {
        inventory.put(slot, itemType);
        saveDataToDatabase();
    }

    public MysteryItemType getInventoryItem(int slot) {
        return inventory.get(slot);
    }

    public Map<Integer, MysteryItemType> getFullInventory() {
        return inventory;
    }

}
