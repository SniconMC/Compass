package rip.snicon.compass.content.bundlebot;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.InteractionMeta;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.player.MysteryPlayer;

public class BundleBotInteractionEntity extends Entity {
    public BundleBotInteractionEntity() {
        super(EntityType.INTERACTION);

        this.editEntityMeta(InteractionMeta.class, interactionMeta -> {
            interactionMeta.setHeight(5);
            interactionMeta.setWidth(5);
            interactionMeta.setResponse(true);
            interactionMeta.setHasNoGravity(true);
        });

        setInstance(MysteryInstanceType.HUB.getInstance(),new Pos(24.5,1.5,102.5));


    }
}
