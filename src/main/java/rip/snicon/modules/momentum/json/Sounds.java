package rip.snicon.modules.momentum.json;

import rip.snicon.Main;

public class Sounds {
    private String sound_event;
    private float volume;
    private float pitch;

    public String getSound_event() {
        return sound_event.toLowerCase();
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
