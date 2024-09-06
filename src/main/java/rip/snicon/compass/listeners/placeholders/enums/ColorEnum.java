package rip.snicon.compass.listeners.placeholders.enums;

import java.util.Random;

public enum ColorEnum {

    RED("#FF0000"),
    GREEN("#00FF00"),
    BLUE("#0000FF"),
    YELLOW("#FFFF00"),
    ORANGE("#FFA500"),
    PURPLE("#800080"),
    CYAN("#00FFFF"),
    MAGENTA("#FF00FF"),
    BLACK("#000000"),
    WHITE("#FFFFFF"),
    GRAY("#808080"),
    SILVER("#C0C0C0"),
    MAROON("#800000"),
    OLIVE("#808000"),
    NAVY("#000080"),
    TEAL("#008080"),
    LIME("#00FF00"),
    AQUA("#00FFFF"),
    FUCHSIA("#FF00FF"),
    CORAL("#FF7F50"),
    SALMON("#FA8072");

    private final String hexCode;

    ColorEnum(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return this.hexCode;
    }

    public static String getRandomColorCode() {
        ColorEnum[] colors = ColorEnum.values(); // Get all enum values
        int randomIndex = new Random().nextInt(colors.length); // Generate a random index
        return colors[randomIndex].getHexCode(); // Return the randomly selected color
    }

}
