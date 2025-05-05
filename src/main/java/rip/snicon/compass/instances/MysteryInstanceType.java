package rip.snicon.compass.instances;

import net.minestom.server.instance.anvil.AnvilLoader;

public enum MysteryInstanceType {
    HUB(new MysteryInstance(new AnvilLoader("resources/worlds/mystery_hub_v5")));

    private final MysteryInstance instance;

    MysteryInstanceType(MysteryInstance instance) {
        this.instance = instance;
    }

    public MysteryInstance getInstance() {
        return instance;
    }
}
