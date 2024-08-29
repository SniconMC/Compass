package rip.snicon.modules.momentum;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.ServerPacket;
import rip.snicon.Main;
import rip.snicon.modules.momentum.json.Coordinates;
import rip.snicon.utils.SoundUtils;

import java.util.HashMap;
import java.util.Map;


public class MomentumExecutor {
    private static final Map<Player, Map<String, Long>> playerCooldowns = new HashMap<>();
    private static final Map<Player, Map<String, BoundingBox>> lastTeleportDestinations = new HashMap<>();

    public static void isOnMomentumPad(MomentumConfig config, Player player, String fileName) {
        Pos playerPos = player.getPosition();

        // TODO: event triggering
        //  1. Camera movement (pitch/yaw) should not count to trigger any pads
        //  2. In the event that a destination for one telepad is also the origin of another:
        //      Player should not teleport to new destination until they have exited the previous destination area

        if (!BoundingBox.isWithinBounds(config, playerPos, true)) {
            return;
        }

        // actually the code is inverted so this checks out
        String type = determinePadType(config); // Determine if it's a telepad or launchpad

        if (!isOnCooldown(config, player, type)) {
            return;
        }

        executeMomentum(config, player, fileName, type);
    }

    private static boolean isOnCooldown(MomentumConfig config, Player player, String type) {
        Map<String, Long> cooldowns = playerCooldowns.get(player);
        if (cooldowns == null) {
            return true; // No cooldown set, player can use the telepad
        }

        Long cooldownEnd = cooldowns.get(type);
        if (cooldownEnd == null) {
            return true; // No cooldown for this type
        }

        long currentTime = System.currentTimeMillis();
        return currentTime >= cooldownEnd; // Check if the cooldown has expired
    }


    private static void setCooldown(Player player, String type, long cooldownDuration) {
        long cooldownEnd = System.currentTimeMillis() + cooldownDuration;
        Map<String, Long> cooldowns = playerCooldowns.getOrDefault(player, new HashMap<>());
        cooldowns.put(type, cooldownEnd);
        playerCooldowns.put(player, cooldowns); // Set cooldown end time for the specific type
    }

    private static void executeMomentum(MomentumConfig config, Player player, String fileName, String type) {
        try {
            if ("telepad".equals(type) && config.getDestination_corners() != null) {
                Coordinates coordinates = config.getDestination_corners();
                Pos corner1 = coordinates.getCorner1();
                Pos corner2 = coordinates.getCorner2();
                Pos destination = getTeleportDestination(corner1, corner2, config, player, fileName);

                MinecraftServer.getSchedulerManager().scheduleNextTick(() -> player.teleport(destination));

                setCooldown(player, "telepad", config.getCooldown());
            } else if ("unknown".equals(type)) {
                Main.logger.error("Type required was telepad, but only " + type + " was found!");
                return;
            }
        } catch (NullPointerException e) {
            Main.logger.error("Destination corners missing! Error: " + e.getMessage());
        }



        Double vertical_strength = config.getVertical_strength();
        Double directional_strength = config.getDirectional_strength();

        try {
            if ("launchpad".equals(type) && vertical_strength != null && directional_strength != null) {
                Pos playerLocation = player.getPosition();

                Vec vector = playerLocation.direction().mul(directional_strength * 20).withY(vertical_strength * 20);
                player.setVelocity(vector);

                setCooldown(player, "launchpad", config.getCooldown());
            } else if ("unknown".equals(type)) {
                Main.logger.error("Type required was launchpad, but only " + type + " was found!");
                return;
            }
        } catch (NullPointerException e) {
            Main.logger.error("Strength value(s) missing! Error: " + e.getMessage());
        }

        String soundEvent = config.getSound().getSound_event();
        String source = config.getSound().getSource();
        float volume = config.getSound().getVolume();
        float pitch = config.getSound().getPitch();

        player.getInstance().playSound(Sound.sound(SoundUtils.stringToSoundEvent(soundEvent), SoundUtils.stringToSource(source), volume, pitch));
    }

    private static String determinePadType(MomentumConfig config) {
        if (config.getDestination_corners() != null) {
            return "telepad";
        } else if (config.getDirectional_strength() != null && config.getVertical_strength() != null) {
            return "launchpad";
        }
        return "unknown";
    }

    private static Pos getTeleportDestination(Pos corner1, Pos corner2, MomentumConfig config, Player player, String fileName) {

        int destination1X = corner1.blockX();
        int destination1Y = corner1.blockY();
        int destination1Z = corner1.blockZ();

        int destination2X = corner2.blockX();
        int destination2Y = corner2.blockY();
        int destination2Z = corner2.blockZ();

        double finalY;

        String isPortal = config.is_portal();

        if ("true".equalsIgnoreCase(isPortal)) {
            // it is a portal, therefore teleport the player to the bottom Y level

            // TODO: telepad height check
            //  implement ability to jump into portal and be teleported to the right place
            //  but with your jump offset included

            finalY = Math.min(corner1.blockY(), corner2.blockY());
        } else if ("false".equalsIgnoreCase(isPortal)) {
            // not a portal
            if (destination1Y != destination2Y) {
                // still has differing Y levels
                Main.logger.warn("Telepad '" + fileName + "' is not a portal, but still has differing Y destinations!");
                Main.logger.warn("Y coordinate defaulting to smaller value");
            }
            finalY = Math.min(corner1.blockY(), corner2.blockY()) + 1;
        } else if (isPortal == null) {
            // Handle the case where is_portal is missing
            Main.logger.warn("Telepad type not declared in the JSON file! Assuming horizontal...");
            finalY = Math.min(corner1.blockY(), corner2.blockY()) + 1;
        } else {
            // Handle the case where is_portal is incorrectly set (e.g., "flase")
            Main.logger.warn("Telepad '" + fileName + "' has invalid is_portal value '" + isPortal + "'. Assuming horizontal...");
            finalY = Math.min(corner1.blockY(), corner2.blockY()) + 1;
        }



        double finalX = ((double) (destination1X + destination2X) / 2) + 0.5;
        double finalZ = ((double) (destination1Z + destination2Z) / 2) + 0.5;

        return new Pos(finalX, finalY, finalZ, config.getTeleport_yaw(), player.getPosition().pitch());
    }

}
