package rip.snicon.utils;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.sound.SoundEvent;
import rip.snicon.Main;

public class SoundUtils {

    public static SoundEvent stringToSoundEvent(String soundName) {
        try {
            return SoundEvent.fromNamespaceId(soundName);
        } catch (IllegalArgumentException e) {
            Main.logger.error("Invalid sound name: " + soundName + ", cause: " + e.getCause());
            Main.logger.info("Switching to default sound event");
            return SoundEvent.BLOCK_GRASS_BREAK;
        }
    }

    public static Sound.Source stringToSource(String soundName) {
        try {
            return Sound.Source.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            Main.logger.error("Invalid sound name: " + soundName + ", cause: " + e.getCause());
            Main.logger.info("Switching to default sound source");
            return Sound.Source.MASTER;
        }
    }

}
