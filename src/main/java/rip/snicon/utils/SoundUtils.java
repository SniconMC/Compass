package rip.snicon.utils;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.sound.SoundEvent;
import rip.snicon.Main;

/**
 * Utility class for converting strings to sound-related types.
 *
 * <p>This class provides methods to safely convert string inputs to
 * {@link SoundEvent} and {@link Sound.Source} objects used in Minecraft server
 * development. It handles invalid input gracefully by logging errors and
 * returning default values.</p>
 *
 * @see SoundEvent
 * @see Sound.Source
 * @see <a href="https://javadoc.minestom.net/">Minestom Documentation</a>
 * @see <a href="https://docs.adventure.kyori.net/">Adventure Documentation</a>
 *
 * @author znopp
 */
public class SoundUtils {

    /**
     * Converts a string to a {@link SoundEvent}.
     *
     * <p>This method attempts to convert the provided string, which represents a sound event's
     * namespace ID, to a {@link SoundEvent} object. If the string is invalid or does not correspond
     * to a valid sound event, the method logs an error and returns a default sound event
     * ({@link SoundEvent#BLOCK_GRASS_BREAK}).</p>
     *
     * @param soundName The string representation of the sound event's namespace ID.
     * @return A {@link SoundEvent} object corresponding to the provided string, or a default sound event if invalid.
     */
    public static SoundEvent stringToSoundEvent(String soundName) {
        try {
            return SoundEvent.fromNamespaceId(soundName);
        } catch (Exception e) {
            Main.logger.warn("Invalid sound name: " + soundName + ", cause: " + e.getMessage());
            Main.logger.warn("Switching to default sound event");
            return SoundEvent.BLOCK_GRASS_BREAK;
        }
    }

    /**
     * Converts a string to a {@link Sound.Source}.
     *
     * <p>This method attempts to convert the provided string to a {@link Sound.Source} enum value.
     * If the string is invalid or does not correspond to a valid sound source, the method logs an
     * error and returns a default sound source ({@link Sound.Source#MASTER}).</p>
     *
     * @param sourceName The string representation of the sound source.
     * @return A {@link Sound.Source} corresponding to the provided string, or a default sound source if invalid.
     */
    public static Sound.Source stringToSource(String sourceName) {
        try {
            return Sound.Source.valueOf(sourceName);
        } catch (Exception e) {
            Main.logger.warn("Invalid source name: " + sourceName + ", cause: " + e.getMessage());
            Main.logger.warn("Switching to default sound source");
            return Sound.Source.MASTER;
        }
    }

}
