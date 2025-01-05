package rip.snicon.compass.utils;

public enum MysteryRarities {
    COMMON("#00FF00"),       // Green
    UNCOMMON("#1EFF00"),     // Lime
    RARE("#0070FF"),         // Blue
    EPIC("#A335EE"),         // Purple
    LEGENDARY("#FF8000"),    // Orange
    MYTHIC("#E6CC80"),       // Gold
    UNIQUE("#FF0000");       // Red

    private final String color;

    MysteryRarities(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
