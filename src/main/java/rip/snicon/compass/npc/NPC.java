package rip.snicon.compass.npc;

import nub.wi1helm.template.npc.TemplateNPC;

public enum NPC {
    DEBUG_NPC(new DebugJohnson()),
    GUIDE_NPC(new GuideNPC()),
    EXAMPLE_NPC(new ExampleJohnson()),
    COSMETICDEALER_NPC(new CosmeticDealerNPC()),
    PARKOUR_NPC(new ParkourNPC()),
    BLOCKHUNT_NPC(new BlockHuntNPC()),
    MINESTOM_NPC(new MinestomNPC());

    private final TemplateNPC npc;

    NPC(TemplateNPC npc) {
        this.npc = npc;
    }

    /**
     * Ensures all NPCs are initialized.
     */
    public static void initializeAll() {
        // Accessing values() ensures all NPCs are created
        NPC[] allNPCs = NPC.values();
    }
}
