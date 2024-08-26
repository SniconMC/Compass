package rip.snicon.modules.momentum;

import rip.snicon.modules.momentum.json.Coordinates;
import rip.snicon.modules.momentum.json.Display;

public class MomentumConfig {

    private Coordinates corners;
    private Coordinates destination_corners;
    private Display display;
    private double directional_strength;
    private double vertical_strength;
    private String world;
    private double teleport_yaw;

    public Coordinates getCorners() {
        return corners;
    }

    public Coordinates getDestination_corners() {
        return destination_corners;
    }

    public Display getDisplay() {
        return display;
    }

    public double getDirectional_strength() {
        return directional_strength;
    }

    public double getVertical_strength() {
        return vertical_strength;
    }

    public String getWorld() {
        return world;
    }

    public double getTeleport_yaw() {
        return teleport_yaw;
    }
}
