package rip.snicon.compass.npc.npcs;


import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.metadata.PlayerMeta;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.npc.MysteryHologram;
import rip.snicon.compass.npc.MysteryNPC;
import rip.snicon.compass.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

public class GuideNPC extends MysteryNPC {

    public GuideNPC() {
        super(
                EntityType.PLAYER, // Entity type from JSON
                3,
                32,
                new Pos(38, 9, 37, 150, 0)
        );
    }

    @Override
    public void initialize() {

        // Add AI group with LookAtPlayerGoal
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new LookAtPlayerGoal(this, 7, getDefaultPos())) // Look at players within 5 blocks
                        .build()
        );

        // Set skin attributes from JSON
        setPlayerSkin(new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTczNTU5MzAxMjM4NywKICAicHJvZmlsZUlkIiA6ICI3NmIwM2FiYjk5YzQ0MzgwOTQ4MDM4ZjFiNGJiYzgxOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGFtc2l0ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hYTU4MWNjNTgxYWNkYjA1ZjFlMWU2NWI4ZDMyNmM1MTBjYTgxNGZmYjEwNjYyYTQ0ZWZlYmVhMmMyOGJiNTBiIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81YzI5NDEwMDU3ZTMyYWJlYzAyZDg3MGVjYjUyZWMyNWZiNDVlYTgxZTc4NWE3ODU0YWU4NDI5ZDcyMzZjYTI2IgogICAgfQogIH0KfQ==", "R2eNRaudWbbu6QbrqWtR9SRdKXmnXfUDAZVsEl+ZWFdVsCr+wahQb849YaBKj8D5avA+TKdTx6EG3CSv/Q60P3fC6g1TaDMZdRvjNy2Q2rYJ8N2glfEUtSljAmVxRNH2gT7dTaSkzCMCvAYp4tGFwzpDG/liATR27oBXUqfvIdNZ20Ao/JeioxPsrKuu+Oj5WiQ354gWSXCJHpGZZQ51/2r807q28vUMtc8vDl0fNtnpkqNMAtdycHr/vi1Z3290++v8L4EkIxCS07CrgxrM7RMDmKmyWSCOp67zECjxlwmyUACBUsst4hGzHT6WatN2tsv6IkgsFMQBlVWt1NmaGWy/N0NNTIxoOgwF/Rd5zUpmFgYyxQ0Tt91w0d1v2SbqG6JjDh8hG7vbaJbL66kdvWnMUjXB2u2wZ2cqTMPm+/yygFsJQ1LLbXPtEyYoVu2/brHpvJx+uh77maJ4qn0xE1hDSSnI0oabaxSPucbFEbX9vkGzSX+MrYlIhIYyf+mIfR9UpR8bjCb01IJV7GyL3NX0SuU1YuN9CLqFJsGQsV73nEdqOuyUj7I9Pgv5MRfRgSgQ+KyI37PpsAHmZHeKWE6WEvOukbkpVOvVsvOYg3CIqrZ6c1gON9GxcMX34ZKSf3pGcEzUz0XY+y+W5w9KE+RB6WDluHebL4kce/4hZwI="));

        // Set player meta
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

    @Override
    public void onSpawnHologram(MysteryHologram hologram, MysteryPlayer player) {
        switch (hologram.getRow()) {
            case 0:
                hologram.setText(TextUtils.convertStringToComponent("<yellow>Right Click</yellow>"));
                break;
            case 1:
                hologram.setText(TextUtils.convertStringToComponent("<green>Guide</green>"));
                break;
            case 2:
                hologram.setText(TextUtils.convertStringToComponent("<white>Browse Ingame Wiki</white>"));
                break;
        }
    }

    @Override
    public void onDespawnHologram(MysteryHologram hologram, MysteryPlayer player) {

    }

    @Override
    public void onSpawn(MysteryPlayer player) {
        setInstance(MysteryInstanceType.HUB.getInstance(), getDefaultPos());
    }

    @Override
    public void onDespawn(MysteryPlayer player) {
        // Handle despawning logic if necessary
    }

    @Override
    public void onInteract(MysteryPlayer player) {
        System.out.println("OldUpdateNPC interacted with " + player.getUsername() + ".");
    }
}

