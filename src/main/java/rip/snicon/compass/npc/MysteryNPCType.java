package rip.snicon.compass.npc;

import rip.snicon.compass.npc.npcs.ExampleJohnson;

public enum MysteryNPCType {
    EXAMPLE_JOHNSON(new ExampleJohnson());

    private final MysteryNPC npcInstance;

    MysteryNPCType(MysteryNPC npcInstance) {
        this.npcInstance = npcInstance;
    }

    public MysteryNPC getNpcInstance() {
        return npcInstance;
    }
}
