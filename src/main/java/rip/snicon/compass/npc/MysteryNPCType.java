package rip.snicon.compass.npc;

import rip.snicon.compass.npc.npcs.BlockHuntNPC;
import rip.snicon.compass.npc.npcs.ExampleJohnson;
import rip.snicon.compass.npc.npcs.GuideNPC;
import rip.snicon.compass.npc.npcs.ParkourNPC;

public enum MysteryNPCType {
    EXAMPLE_JOHNSON(new ExampleJohnson()),
    BLOCKHUNT_NPC(new BlockHuntNPC()),
    PARKOUR_NPC(new ParkourNPC()),
    GUIDE_NPC(new GuideNPC());

    private final MysteryNPC npcInstance;

    MysteryNPCType(MysteryNPC npcInstance) {
        this.npcInstance = npcInstance;
        this.npcInstance.setNpcIdentifier(this.name());
    }

    public MysteryNPC getNpcInstance() {
        return npcInstance;
    }
}
