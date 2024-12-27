package rip.snicon.compass.npc.npcs;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.metadata.PlayerMeta;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.npc.MysteryNPC;
import rip.snicon.compass.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.player.MysteryPlayer;

// Example NPC Class
public class ExampleJohnson extends MysteryNPC {

    public ExampleJohnson() {
        super(EntityType.PLAYER, "ExampleJohnson", 10);
    }

    @Override
    protected boolean spawnCondition() {
        return true;
    }

    @Override
    public void initialize(MysteryPlayer player) {
        player.sendMessage("obama");
        setInstance(MysteryInstanceType.HUB.getInstance(), player.getPosition());

        setPlayerSkin(player.getSkin());

        // Add AI group with LookAtPlayerGoal
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new LookAtPlayerGoal(this, 5)) // Look at players within 5 blocks
                        .build()
        );

        if (entityType == EntityType.PLAYER) {
            editEntityMeta(PlayerMeta.class, meta -> {
                meta.setCapeEnabled(false);
                meta.setJacketEnabled(true);
                meta.setLeftSleeveEnabled(true);
                meta.setRightSleeveEnabled(true);
                meta.setLeftLegEnabled(true);
                meta.setRightLegEnabled(true);
                meta.setHatEnabled(true);
            });
        }

    }

    @Override
    public void onSpawn() {
        System.out.println("ExampleJohnson spawned.");
    }

    @Override
    public void onDisappear() {
        System.out.println("ExampleJohnson disappeared.");
    }

    @Override
    public void onInteract(MysteryPlayer player) {
        System.out.println("ExampleJohnson interacted with " + player.getUsername() + ".");
    }
}
