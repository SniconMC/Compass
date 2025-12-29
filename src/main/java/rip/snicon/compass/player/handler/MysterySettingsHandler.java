package rip.snicon.compass.player.handler;

import nub.wi1helm.smoxy.mongodb.MongoDatabaseManager;
import org.bson.Document;
import rip.snicon.compass.player.data.settings.PlayerSetting;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class MysterySettingsHandler {

    private final UUID uuid;
    private final Map<PlayerSetting, Boolean> settings = new EnumMap<>(PlayerSetting.class);

    public MysterySettingsHandler(UUID uuid) {
        this.uuid = uuid;
        loadDefaultSettings();
    }

    /**
     * Fetches settings from the database.
     */
    public void fetchSettingsFromDatabase() {
        MongoDatabaseManager.loadDocumentAsync("settings", uuid.toString()).thenAccept(document -> {
            if (document != null) {
                loadSettingsData(document);
            } else {
                saveSettingsToDatabase(); // Save defaults if no settings exist
            }
        });
    }

    /**
     * Saves settings to the database.
     */
    public void saveSettingsToDatabase() {
        Document settingsDocument = new Document("uuid", uuid.toString());

        // Add each setting to the document
        settings.forEach((key, value) -> settingsDocument.append(key.name(), value));

        MongoDatabaseManager.saveDocumentAsync("settings", "uuid", settingsDocument).exceptionally(throwable -> {
            System.err.println("Failed to save settings: " + throwable.getMessage());
            return null;
        });
    }

    /**
     * Loads settings from the database document.
     *
     * @param document the document containing the settings data
     */
    private void loadSettingsData(Document document) {
        for (PlayerSetting setting : PlayerSetting.values()) {
            if (document.containsKey(setting.name())) {
                settings.put(setting, document.getBoolean(setting.name()));
            }
        }
    }

    /**
     * Loads default settings into the handler.
     */
    private void loadDefaultSettings() {
        for (PlayerSetting setting : PlayerSetting.values()) {
            settings.put(setting, setting.getDefaultValue());
        }
    }

    /**
     * Gets the value of a specific setting.
     *
     * @param setting the setting to retrieve
     * @return the value of the setting
     */
    public boolean getSetting(PlayerSetting setting) {
        return settings.getOrDefault(setting, setting.getDefaultValue());
    }

    /**
     * Updates a setting and saves the change to the database.
     *
     * @param setting the setting to update
     * @param value   the new value of the setting
     */
    public void updateSetting(PlayerSetting setting, boolean value) {
        settings.put(setting, value);
        saveSettingsToDatabase();
    }

    /**
     * Resets all settings to their default values and saves them to the database.
     */
    public void resetSettingsToDefault() {
        loadDefaultSettings();
        saveSettingsToDatabase();
    }
}
