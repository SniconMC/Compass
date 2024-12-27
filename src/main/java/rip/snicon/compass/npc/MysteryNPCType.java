package rip.snicon.compass.npc;

import rip.snicon.compass.npc.npcs.BlockHuntNPC;
import rip.snicon.compass.npc.npcs.ExampleJohnson;
import rip.snicon.compass.npc.npcs.ParkourNPC;

public enum MysteryNPCType {
    EXAMPLE_JOHNSON(new ExampleJohnson()),
    BLOCKHUNT_NPC(new BlockHuntNPC()),
    PARKOUR_NPC(new ParkourNPC());

    private final MysteryNPC npcInstance;

    MysteryNPCType(MysteryNPC npcInstance) {
        this.npcInstance = npcInstance;
    }

    public MysteryNPC getNpcInstance() {
        return npcInstance;
    }
}
