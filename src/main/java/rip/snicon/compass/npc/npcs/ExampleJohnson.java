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

import java.util.List;

// Example NPC Class
public class ExampleJohnson extends MysteryNPC {

    public ExampleJohnson() {
        super(EntityType.PLAYER, 1, 32, new Pos(40,9,25,69,0));
    }


    @Override
    public void initialize() {

        // Add AI group with LookAtPlayerGoal
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new LookAtPlayerGoal(this, 7, getDefaultPos())) // Look at players within 5 blocks
                        .build()
        );


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
                hologram.setText(TextUtils.convertStringToComponent("<green>Example Johnson</green>"));
        }
    }

    @Override
    public void onDespawnHologram(MysteryHologram hologram, MysteryPlayer player) {

    }

    @Override
    public void onSpawn(MysteryPlayer player) {
        ;
        setInstance(MysteryInstanceType.HUB.getInstance(), getDefaultPos());

        setPlayerSkin(new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTYxNjU0Mjk5MTMyNCwKICAicHJvZmlsZUlkIiA6ICIwNWQ0NTNiZWE0N2Y0MThiOWI2ZDUzODg0MWQxMDY2MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJFY2hvcnJhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EzYjg5NTY2MmI1OWE0ODliY2ZkYWU0NTIxNDJkMWM5MWVjYjExNmYxYTQ4ZjA0NDJlYTQwZDdiMjg4OGYzOGEiCiAgICB9CiAgfQp9", "TNSXq/sjg65pAfdQ1kPcVuM38OeVudt/63nTRcoCrutPDIg3mhcFJDJ4G9xzYv7u4pqjRzMoDPgVufXdGQMa2S+i9GsyzaybA0YsNiZMfm4LKpbtpDv/224pefK+5adOLM8JGL0z92dLgAdEZ7ybWF7GdEoG126tqRRIYBpO1mHggY+xeK/CPJ3O/eDHGV0k5loxLlO5qL12c2q4Rdz0nZuzXGLzAZERKLcajrq5fkuZOILW9kr2FtbhuczOEP0T9pdRRb255WFKU0LSG+RDG6j7AuKr7hQMIFZ0/3X94f6ymY7nwOnKJxSGFPyZ7F5+yXE6NJaswRlvIIvgQpYT7kp6/YnRdYp5rFh2RyczAbKKFFQf8vWAT16drqf7pE+zyPdxkPkO381Q7PtVzG+dcg5/MqD3+YQhYwUk14e7C/ZZ12Rc0Pb3HINbEKDsspIPSot+uAbeQEiemBADi8HvPUuBIxHq2LkasPu9NDtKhTqy+sE2RNelWIBXxZCJEU0jJRLMJSEQOFRnr4ZOWq2soAcJehbfHimXTckbS48s4oWEB+kpGVYrfNS0muYUPzOi8VOyuEteARI1HYqJhqaqw/tTm34uEm1BX070/s3DmmOd8L1JXmEjQvW8vMbwNF76LQ73bv4lANYGNU1bRyAznXtimk43Lbw2vF1Vrd/hq8w="));

    }

    @Override
    public void onDespawn(MysteryPlayer player) {

    }

    @Override
    public void onInteract(MysteryPlayer player) {
        System.out.println("ExampleJohnson interacted with " + player.getUsername() + ".");
    }
}
