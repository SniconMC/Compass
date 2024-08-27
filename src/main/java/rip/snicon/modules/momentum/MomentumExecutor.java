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
    private static final Map<Player,Map<String, Long>> lastExecuteTime = new HashMap<>();

    public static void isOnMomentumPad(MomentumConfig config, Player player, String fileName) {
        Pos playerPos = player.getPosition();

        if (!isOnBlock(config, playerPos)) {
            return;
        }
        Main.logger.info("Player on block");

        // actually the code is inverted so this checks out
        if (!isOnCoolDown(config ,player, fileName)) {
            Main.logger.warn("Player is on cooldown, returning");
            return;
        }
        executeMomentum(config, player, fileName);
        setCoolDown(player, fileName);
    }

    private static boolean isOnBlock(MomentumConfig config, Pos playerLocation) {
        // Get the corners from the configuration
        Pos corner1 = config.getCorners().getCorner1();
        Pos corner2 = config.getCorners().getCorner2();

        // Calculate the minimum and maximum bounds for X, Y, Z
        int minX = Math.min(corner1.blockX(), corner2.blockX());
        int maxX = Math.max(corner1.blockX(), corner2.blockX());

        double minY = Math.min(corner1.blockY(), corner2.blockY());
        double maxY = Math.max(corner1.blockY(), corner2.blockY()) + 1;

        int minZ = Math.min(corner1.blockZ(), corner2.blockZ());
        int maxZ = Math.max(corner1.blockZ(), corner2.blockZ());

        // Check if the player's position is within the bounds
        return playerLocation.blockX() >= minX && playerLocation.blockX() <= maxX &&
                playerLocation.y() >= minY && playerLocation.y() <= maxY &&
                playerLocation.blockZ() >= minZ && playerLocation.blockZ() <= maxZ;
    }

    private static boolean isOnCoolDown(MomentumConfig config, Player player, String fileName) {
        Map<String, Long> map = lastExecuteTime.get(player);
        if (map == null) {
            return true;
        }
        Long lastExecute = map.get(fileName);
        if (lastExecute == null) {
            return true;
        }

        long timeSinceLastExecute = System.currentTimeMillis() - lastExecute;
        return timeSinceLastExecute >= config.getCooldown();
    }

    private static void setCoolDown(Player player, String fileName) {

        long currentTime = System.currentTimeMillis();
        Map<String, Long> map = new HashMap<>();
        map.put(fileName, currentTime);
        lastExecuteTime.put(player, map);
    }

    private static void executeMomentum(MomentumConfig config, Player player, String fileName) {
        Main.logger.info("Executing momentum");

        if (config.getDestination_corners() != null) {
            Main.logger.info("we found the destination corners");
            Coordinates coordinates = config.getDestination_corners();
            Pos destination = getTeleportDestination(coordinates.getCorner1(), coordinates.getCorner2(), config, player, fileName);
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> player.teleport(destination));
        }

        Double vertical_strength = config.getVertical_strength();
        Double directional_strength = config.getDirectional_strength();

        if (vertical_strength != null && directional_strength != null) {
            Main.logger.info("we found the strength");
            Pos playerLocation = player.getPosition();

            Vec vector = playerLocation.direction().mul(directional_strength * 20).withY(vertical_strength * 20);
            player.setVelocity(vector);

        }

        String soundEvent = config.getSound().getSound_event();
        String source = config.getSound().getSource();
        float volume = config.getSound().getVolume();
        float pitch = config.getSound().getPitch();

        //player.getInstance().playSound(Sound.sound(SoundUtils.stringToSoundEvent(soundEvent), SoundUtils.stringToSource(source), volume, pitch));

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

            // TODO
            //  implement ability to jump into portal and be teleported to the right place
            //  but with your jump offset included

            finalY = Math.min(corner1.blockY(), corner2.blockY());
        } else if ("false".equalsIgnoreCase(isPortal)) {
            // not a portal
            if (destination1Y != destination2Y) {
                // still has differing Y levels
                Main.logger.warn("Telepad '" + fileName + "' is not a portal, but still has differing Y destinations!");
                Main.logger.warn("Y coordinate defaulting to smaller value...");
            }
            finalY = Math.min(corner1.blockY(), corner2.blockY()) + 1;
        } else if (isPortal == null) {
            // Handle the case where is_portal is missing
            Main.logger.warn("Telepad type not declared in the JSON file! Assuming horizontal...");
            finalY = Math.min(corner1.blockY(), corner2.blockY()) + 1;
        } else {
            // Handle the case where is_portal is incorrectly set (e.g., "flase")
            Main.logger.warn("Telepad '" + fileName + "' has an invalid is_portal value: '" + isPortal + "'. Assuming horizontal...");
            finalY = Math.min(corner1.blockY(), corner2.blockY()) + 1;
        }



        double finalX = ((double) (destination1X + destination2X) / 2) + 0.5;
        double finalZ = ((double) (destination1Z + destination2Z) / 2) + 0.5;

        return new Pos(finalX, finalY, finalZ, config.getTeleport_yaw(), player.getPosition().pitch());
    }

}
