package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.metadata.PlayerMeta;
import nub.wi1helm.template.npc.SkinLayer;
import nub.wi1helm.template.npc.SpawnStrategy;
import nub.wi1helm.template.npc.TemplateNPC;
import nub.wi1helm.template.npc.TemplateText;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.utils.TextUtils;

public class ExampleJohnson extends TemplateNPC {

    public ExampleJohnson() {
        super(EntityType.PLAYER);

        // Set Spawn Position
        setSpawnPosition(new Pos(44.0, 9.5, 26.8, 0.0f, 0.0f));
        setSpawnStrategy(SpawnStrategy.SITTING);
        // Set Skin Layers
        setSkinLayer(SkinLayer.NO_CAPE);

        // Set Skin
        setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTYxNjU0Mjk5MTMyNCwKICAicHJvZmlsZUlkIiA6ICIwNWQ0NTNiZWE0N2Y0MThiOWI2ZDUzODg0MWQxMDY2MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJFY2hvcnJhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EzYjg5NTY2MmI1OWE0ODliY2ZkYWU0NTIxNDJkMWM5MWVjYjExNmYxYTQ4ZjA0NDJlYTQwZDdiMjg4OGYzOGEiCiAgICB9CiAgfQp9",
                "TNSXq/sjg65pAfdQ1kPcVuM38OeVudt/63nTRcoCrutPDIg3mhcFJDJ4G9xzYv7u4pqjRzMoDPgVufXdGQMa2S+i9GsyzaybA0YsNiZMfm4LKpbtpDv/224pefK+5adOLM8JGL0z92dLgAdEZ7ybWF7GdEoG126tqRRIYBpO1mHggY+xeK/CPJ3O/eDHGV0k5loxLlO5qL12c2q4Rdz0nZuzXGLzAZERKLcajrq5fkuZOILW9kr2FtbhuczOEP0T9pdRRb255WFKU0LSG+RDG6j7AuKr7hQMIFZ0/3X94f6ymY7nwOnKJxSGFPyZ7F5+yXE6NJaswRlvIIvgQpYT7kp6/YnRdYp5rFh2RyczAbKKFFQf8vWAT16drqf7pE+zyPdxkPkO381Q7PtVzG+dcg5/MqD3+YQhYwUk14e7C/ZZ12Rc0Pb3HINbEKDsspIPSot+uAbeQEiemBADi8HvPUuBIxHq2LkasPu9NDtKhTqy+sE2RNelWIBXxZCJEU0jJRLMJSEQOFRnr4ZOWq2soAcJehbfHimXTckbS48s4oWEB+kpGVYrfNS0muYUPzOi8VOyuEteARI1HYqJhqaqw/tTm34uEm1BX070/s3DmmOd8L1JXmEjQvW8vMbwNF76LQ73bv4lANYGNU1bRyAznXtimk43Lbw2vF1Vrd/hq8w="
        ));

        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        // Set AI Goal (Look at players)
        setGoal(new LookAtPlayerGoal(this, 7, getSpawnPosition()));

        // Set Name (Hologram-like Text)
        setName(new TemplateText(
                TextUtils.convertStringToComponent("<green>Example Johnson</green>")
        ));
    }

    @Override
    protected void personalize(Player player) {
        System.out.println("ExampleJohnson personalized for " + player.getUsername() + ".");
    }
}
