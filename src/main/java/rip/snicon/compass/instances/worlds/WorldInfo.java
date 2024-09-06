package rip.snicon.compass.instances.worlds;

import net.minestom.server.coordinate.Pos;

public class WorldInfo {

    private double spawnX;
    private double spawnY;
    private double spawnZ;
    private float spawnPitch;
    private float spawnYaw;

    private boolean hasVoidLimit;
    private double voidLimitHeight;

    private boolean blockUpdates; // Will not yet be implemented.

    private String weather;

    private String time;

    private boolean doDaylightCycle;

    private String defaultGamemode;


    public double getSpawnX() {
        return spawnX;
    }

    public double getSpawnY() {
        return spawnY;
    }

    public double getSpawnZ() {
        return spawnZ;
    }

    public Pos getSpawn(){
        return new Pos(spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);
    }

    public float getSpawnPitch() {
        return spawnPitch;
    }

    public float getSpawnYaw() {
        return spawnYaw;
    }

    public boolean isHasVoidLimit() {
        return hasVoidLimit;
    }

    public double getVoidLimitHeight() {
        return voidLimitHeight;
    }

    public boolean isBlockUpdates() {
        return blockUpdates;
    }

    public String getDefaultGamemode() {
        return defaultGamemode;
    }

    public String getWeather() {
        return weather;
    }

    public String getTime() {
        return time;
    }

    public boolean isDoDaylightCycle() {
        return doDaylightCycle;
    }

}
