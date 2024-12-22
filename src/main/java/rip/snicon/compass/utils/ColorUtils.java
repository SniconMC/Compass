package rip.snicon.compass.utils;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import rip.snicon.compass.Main;

/**
 * Utility class for handling color-related operations such as validation and conversion
 * between different color representations (e.g., hex strings, named colors, and RGB values).
 *
 * <p>This class is designed to support color operations within a Minecraft server or any
 * application using the Kyori Adventure API for text formatting and color management.</p>
 *
 * @see NamedTextColor
 * @see TextColor
 * @see <a href="https://docs.adventure.kyori.net/">Kyori Adventure Documentation</a>
 *
 * @author znopp
 */

public class ColorUtils {

    /**
     * Validates whether a given string is a valid hex color code.
     *
     * <p>A valid hex color code starts with a '#' followed by exactly six hexadecimal
     * digits (0-9, A-F, a-f).</p>
     *
     * @param hexColorCode The hex color code string to validate (e.g., "#FFFFFF").
     * @return {@code true} if the input string is a valid hex color code, {@code false} otherwise.
     */

    public static boolean isValidHexColorCode(String hexColorCode) {
        return hexColorCode.matches("^#[0-9A-Fa-f]{6}$");
    }

    /**
     * Converts a hex color string or a named color string to a {@link TextColor} object.
     *
     * <p>This method supports two formats:
     * <ul>
     *   <li>Hex color strings starting with '#' (e.g., "#FF0000" for red).</li>
     *   <li>Named color strings (e.g., "red", "blue", "yellow").</li>
     * </ul>
     *
     * <p>If the input is null or an invalid format, the method defaults to {@link NamedTextColor#WHITE}.</p>
     *
     * @param hex The color string to convert. Can be a hex string (e.g., "#FF00FF") or a named color (e.g., "red").
     * @return The corresponding {@link TextColor} object. Defaults to gray if input is null or invalid.
     *
     * @author znopp
     */

    public static TextColor StringToTextColor(String hex) {
        if (hex == null){
            return TextColor.color(0xAAAAAA);
        }
        if (hex.startsWith("#") && hex.length() == 7) {
            try {
                int color = Integer.parseInt(hex.substring(1), 16);
                return TextColor.color(color);
            } catch (NumberFormatException e) {
                Main.logger.warn("Invalid hex color code: " + hex);
                Main.logger.warn("Defaulting to gray...");
                return TextColor.color(0xAAAAAA);
            }
        }

        NamedTextColor namedColor = NamedTextColor.NAMES.value(hex.toLowerCase());

        // Return the named color if found; otherwise, return a default color
        return (namedColor != null) ? namedColor : NamedTextColor.WHITE;
    }

    /**
     * Converts a hex color string or a named color string to an RGB integer.
     *
     * <p>This method supports two formats:
     * <ul>
     *   <li>Hex color strings starting with '#' (e.g., "#FF0000" for red).</li>
     *   <li>Named color strings (e.g., "red", "blue", "yellow").</li>
     * </ul>
     *
     * <p>If the input is null or an invalid format, the method defaults to a light gray color (0xAAAAAA).</p>
     *
     * @param color The color string to convert. Can be a hex string (e.g., "#FF5555") or a named color (e.g., "red").
     * @return The corresponding RGB integer. Defaults to 0xAAAAAA if input is null or invalid.
     */

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