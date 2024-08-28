package rip.snicon.modules.momentum;

import net.minestom.server.coordinate.Pos;
import rip.snicon.Main;

public class BoundingBox {

    private final Pos corner1;
    private final Pos corner2;

    public BoundingBox(Pos corner1, Pos corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public static boolean isWithinBounds(MomentumConfig config, Pos playerLocation, boolean checkY) {
        Pos corner1 = config.getCorners().getCorner1();
        Pos corner2 = config.getCorners().getCorner2();
        return isWithinBounds(corner1, corner2, playerLocation, checkY);
    }

    public boolean isWithinBoundsNoConfig(Pos playerLocation, boolean checkY) {
        return isWithinBounds(this.corner1, this.corner2, playerLocation, checkY);
    }

    private static boolean isWithinBounds(Pos corner1, Pos corner2, Pos playerLocation, boolean checkY) {
        int minX = Math.min(corner1.blockX(), corner2.blockX());
        int maxX = Math.max(corner1.blockX(), corner2.blockX());
        int minZ = Math.min(corner1.blockZ(), corner2.blockZ());
        int maxZ = Math.max(corner1.blockZ(), corner2.blockZ());

        if (checkY) {
            double minY = Math.min(corner1.blockY(), corner2.blockY());
            double maxY = Math.max(corner1.blockY(), corner2.blockY()) + 1;

            return playerLocation.blockX() >= minX && playerLocation.blockX() <= maxX &&
                    playerLocation.y() >= minY && playerLocation.y() <= maxY &&
                    playerLocation.blockZ() >= minZ && playerLocation.blockZ() <= maxZ;
        } else {

            Main.logger.debug("didn't check Y");
            return playerLocation.blockX() >= minX && playerLocation.blockX() <= maxX &&
                    playerLocation.blockZ() >= minZ && playerLocation.blockZ() <= maxZ;
        }


    }
}