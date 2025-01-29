package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import nub.wi1helm.template.npc.*;
import nub.wi1helm.template.npc.goals.LookAtPlayerGoal;
import org.w3c.dom.Text;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.utils.TextUtils;

public class MinestomNPC extends TemplateNPC {
    public MinestomNPC() {
        super(EntityType.PLAYER);

        setSkin(new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTczODE2OTQ5MjI1NywKICAicHJvZmlsZUlkIiA6ICI1MDMyYTA2NWQ5MWQ0NTgyYjZmODM0MDRlMGYyOTA4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYWNCb29rUHJvTTIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjE0MWQzYTFkYmNkZTZjYTk5OTU5ZmU0YWMwOTE0N2EwN2I1OGYyYjk4OWQxMWMzNzYwMDFlNzJlOTUwZDhiZSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9", "P2mjPyA/S+cMlA97rY5dq8YNF5DijHgxwAWUmcWele+IJc7kofpzpiYvpQrUuov4DkApeF83UeMJs0xXfsk+ieGd0bHinipQqs3Ik7fA+pgu/uBgIxM6Hs9C1uwzVul6gXnlD1DB4kTVZ0cpwWPZk94/R+U2afIjnCO+zeN5EWtKE82+wc+MRVMLI1F3EFgRljk0pN7igBi/WR6dSpmxqQ2gjEDd3xhCypeoKsgn2BkwERn7JFezz4t3vm+13xPRToCbM1IRprfeemcgDokE9cp2uPcmiKmHhjR5B/T3A6TquKpOxeNt7kvvzcbECdSEjmTvttw4a4R/OFgivPFOy7sSpALagF0zb40fikbBd2D1puaKVnU8oIQm6arh3V6FYkz3JuOvheV1UULgITvyp+BhYawwHJeipqbfuODxb3GYQ+zqRHkL/eVnx/6rvbZ+3hWlexWyRc1kzKthtQsVbRKctYUnDs+KXg/VyLWKwrAcmyhiXrlJXwT1PW7f1Ds9QC2KKm9y/7y99qhhJOBaPh9DAiYJOgVlgs4tugjCXKveigYH9L4Y3M6jIJERzOWmsE+rY9ORV9IHOp+5CIMrzCG02mNTETy96P8YfUb8U4KNiI/GmM2Jl1GRpGw0X1DOUs+7yM2EyFPPJOyRne9fCF5LcKQU6GA5jOeJrwIak3Q="));

        setSpawnPosition(new Pos( 22.5,9,46.5, 135, 0));
        setSpawnStrategy(SpawnStrategy.SITTING);

        setName(new TemplateText(
                TextUtils.convertStringToComponent("<gradient:#ff6f5c:#ff76b6>Created With Minestom</gradient>"),
                TextUtils.convertStringToComponent("This server is")
        ));

        // Set Instance
        setInstance(MysteryInstanceType.HUB.getInstance(), getSpawnPosition());

        setSkinLayer(SkinLayer.NO_CAPE);

        setGoal(new LookAtPlayerGoal(this,7, getSpawnPosition()));
    }

    @Override
    protected void personalize(Player player) {
        setActionList(new ActionList(

        ));
    }
}
