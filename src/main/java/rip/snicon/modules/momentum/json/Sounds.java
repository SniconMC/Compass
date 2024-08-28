package rip.snicon.modules.momentum.json;

import rip.snicon.Main;

public class Sounds {
    private String source;
    private String sound_event;
    private float volume;
    private float pitch;

    public String getSource() {
        return source.toUpperCase();
    }

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
