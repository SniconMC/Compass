package rip.snicon.compass.player.data;

import java.util.Map;

public enum MysteryRarities {
    COMMON("#FFFFFF"),       // Green
    UNCOMMON("#55FF55"),     // Lime
    RARE("#5555FF"),         // Blue
    EPIC("#AA00AA"),         // Purple
    LEGENDARY("#FFAA00"),    // Orange
    MYTHIC("#FF55FF"),       // Gold
    UNIQUE("#FF5555");       // Red

    private final String color;

    MysteryRarities(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static final Map<MysteryRarities, Integer> RARITY_PRIORITY = Map.of(
            MysteryRarities.UNIQUE,     0,
            MysteryRarities.MYTHIC,     1,
            MysteryRarities.LEGENDARY,  2,
            MysteryRarities.EPIC,       3,
            MysteryRarities.RARE,       4,
            MysteryRarities.UNCOMMON,   5,
            MysteryRarities.COMMON,     6
    );

}
