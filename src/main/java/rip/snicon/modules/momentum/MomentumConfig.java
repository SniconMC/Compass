package rip.snicon.modules.momentum;

import rip.snicon.modules.momentum.json.Coordinates;
import rip.snicon.modules.momentum.json.Display;
import rip.snicon.modules.momentum.json.Sounds;

public class MomentumConfig {

    private Coordinates corners;
    private Coordinates destination_corners;
    private Display display;
    private Sounds sound;

    private Double directional_strength;
    private Double vertical_strength;
    private String world;
    private float teleport_yaw;
    private Long cooldown;
    private String is_portal;

    public Coordinates getCorners() {
        return corners;
    }

    public Coordinates getDestination_corners() {
        return destination_corners;
    }

    public Display getDisplay() {
        return display;
    }

    public Double getDirectional_strength() {
        return directional_strength;
    }

    public Double getVertical_strength() {
        return vertical_strength;
    }

    public String getWorld() {
        return world;
    }

    public float getTeleport_yaw() {
        return teleport_yaw;
    }

    public Long getCooldown() {
        return cooldown;
    }

    public Sounds getSound() {
        return sound;
    }

    public String is_portal() {
        return is_portal;
    }
}
