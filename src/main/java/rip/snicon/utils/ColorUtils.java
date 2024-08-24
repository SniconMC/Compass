package rip.snicon.utils;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class ColorUtils {

    public static boolean isValidHexColorCode(String hexColorCode) {
        return hexColorCode.matches("^#[0-9A-Fa-f]{6}$");
    }

    // Method to convert hex color string to TextColor
    public static TextColor StringToTextColor(String hex) {
        if (hex == null){
            return TextColor.color(0xAAAAAA);
        }
        if (hex.startsWith("#") && hex.length() == 7) {
            try {
                int color = Integer.parseInt(hex.substring(1), 16);
                return TextColor.color(color);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return switch (hex.toLowerCase()) {
            case "black" -> NamedTextColor.BLACK;
            case "dark_blue" -> NamedTextColor.DARK_BLUE;
            case "dark_green" -> NamedTextColor.DARK_GREEN;
            case "dark_aqua" -> NamedTextColor.DARK_AQUA;
            case "dark_red" -> NamedTextColor.DARK_RED;
            case "dark_purple" -> NamedTextColor.DARK_PURPLE;
            case "gray", "grey" -> NamedTextColor.GRAY;
            case "dark_gray", "dark_grey" -> NamedTextColor.DARK_GRAY;
            case "blue" -> NamedTextColor.BLUE;
            case "green" -> NamedTextColor.GREEN;
            case "aqua" -> NamedTextColor.AQUA;
            case "red" -> NamedTextColor.RED;
            case "gold" -> NamedTextColor.GOLD;
            case "light_purple" -> NamedTextColor.LIGHT_PURPLE;
            case "yellow" -> NamedTextColor.YELLOW;
            case "white" -> NamedTextColor.WHITE;
            default ->
                // Handle unknown colors
                    NamedTextColor.WHITE; // Default to white or handle as needed
        };
    }
    // Method to convert hex color string or named color to RGB integer
    public static int StringToRgb(String color) {
        if (color == null) {
            return 0xAAAAAA; // Default color if input is null
        }

        // Handle hex color strings
        if (color.startsWith("#") && color.length() == 7) {
            try {
                // Parse hex string to integer
                return Integer.parseInt(color.substring(1), 16);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Handle named colors (fallback)
        return switch (color.toLowerCase()) {
            case "black" -> 0x000000;
            case "dark_blue" -> 0x0000AA;
            case "dark_green" -> 0x00AA00;
            case "dark_aqua" -> 0x00AAAA;
            case "dark_red" -> 0xAA0000;
            case "dark_purple" -> 0xAA00AA;
            case "gray" -> 0xAAAAAA;
            case "dark_gray" -> 0x555555;
            case "blue" -> 0x5555FF;
            case "green" -> 0x55FF55;
            case "aqua" -> 0x55FFFF;
            case "red" -> 0xFF5555;
            case "gold" -> 0xFFAA00;
            case "light_purple" -> 0xFF55FF;
            case "yellow" -> 0xFFFF55;
            case "white" -> 0xFFFFFF;
            default -> 0xFFFFFF; // Default to white or handle as needed
        };
    }

}
