package rip.snicon.modules.oblivion.json;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.*;
import org.jetbrains.annotations.NotNull;
import rip.snicon.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisplayText extends Entity {

    private String uuid;
    private Integer rowId;
    private int entityId;
    private Pos parentPos;
    private List<String> text;

    public DisplayText(int rowId,@NotNull Pos position, @NotNull Instance instance, List<String> text) {
        super(EntityType.TEXT_DISPLAY);

        this.uuid = String.valueOf(UUID.randomUUID());
        this.rowId = rowId;
        this.entityId = (int) (Math.random() * Integer.MAX_VALUE);
        this.parentPos = position;
        this.text = text;
        Pos spawnPos = parentPos.add(0,rowId*0.4,0);

        setInstance(instance, spawnPos);

        setText(this);
    }

    // Send packets to make the NPC visible only to a specific player
    public void makeVisibleTo(@NotNull Player player) {
        var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();

        // Send the SpawnEntityPacket only to this player
        player.sendPacket(new SpawnEntityPacket(
                getEntityId(),
                UUID.fromString(uuid),
                EntityType.TEXT_DISPLAY.id(),
                position,
                position.yaw(),
                0,
                (short) 0,  // velocity x
                (short) 0,  // velocity y
                (short) 0   // velocity z
        ));
    }

    // Despawn the NPC for a specific player
    public void despawnForPlayer(@NotNull Player player) {
        player.sendPacket(new DestroyEntitiesPacket(getEntityId()));
        this.remove();
    }

    private void setText(Entity entity) {

        TextDisplayMeta meta = (TextDisplayMeta) entity.getEntityMeta();
        meta.setText(TextUtils.convertStringToComponent(text));

    }
}
