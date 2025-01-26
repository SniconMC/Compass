package rip.snicon.compass.npc;

import rip.snicon.compass.npc.npcs.*;

public enum MysteryNPCType {
    EXAMPLE_JOHNSON(new ExampleJohnson()),
    BLOCKHUNT_NPC(new BlockHuntNPC()),
    PARKOUR_NPC(new ParkourNPC()),
    GUIDE_NPC(new GuideNPC()),
    DEBUG_NPC(new DebugJohnson()),
    COSMETIC_DEALER_NPC(new CosmeticDealerNPC());

    private final MysteryNPC npcInstance;

    MysteryNPCType(MysteryNPC npcInstance) {
        this.npcInstance = npcInstance;
        this.npcInstance.setNpcIdentifier(this.name());
    }

    public MysteryNPC getNpcInstance() {
        return npcInstance;
    }
}
