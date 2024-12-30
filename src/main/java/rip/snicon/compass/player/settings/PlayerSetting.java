package rip.snicon.compass.player.settings;

public enum PlayerSetting {
    SHOW_ICON( true, "Toggles between showing professions icon or its name."),
    SHOW_PLAYERS( true, "Toggles between showing and hiding players."),
    DECIMAL_NUMBERS(false,"A toggle to swtich between numbers and procent in menus when displaying stats");


    private final boolean defaultValue;
    private final String description;

    PlayerSetting(boolean value, String description) {

        this.defaultValue = value;
        this.description = description;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public String getDescription(){
        return description;
    }
}
