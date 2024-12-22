package rip.snicon.compass.player.settings;

public enum PlayerSetting {
    SHOW_ICON("show_icon", true, "Toggles between showing professions icon or its name."),
    SHOW_PLAYERS("show_players", true, "Toggles between showing and hiding players.");
    private final String key;
    private final boolean defaultValue;
    private final String description;

    PlayerSetting(String key, boolean defaultValue, String description) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public String getDescription(){
        return description;
    }
}
