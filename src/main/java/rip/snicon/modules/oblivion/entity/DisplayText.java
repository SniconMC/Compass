package rip.snicon.modules.oblivion.entity;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.LazyPacket;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.nbt.BinaryTagSerializer;
import net.minestom.server.utils.nbt.BinaryTagWriter;
import org.jetbrains.annotations.NotNull;
import rip.snicon.Main;
import rip.snicon.utils.TextUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DisplayText extends Entity {

    private String uuid;
    private Integer rowId;
    private int entityId;
    private Pos parentPos;
    private List<String> text;

    public DisplayText(int rowId, @NotNull Pos position, List<String> text) {
        super(EntityType.TEXT_DISPLAY);
        this.rowId = rowId;
        this.position = position.add(0,rowId*0.4,0);
        this.text = text;

        editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setHasNoGravity(true);
            meta.setText(TextUtils.convertStringToComponent(text));
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        });
    }

    // Send packets to make the NPC visible only to a specific player
    public void makeVisibleTo(@NotNull Player player) {
        Map<Integer, Metadata.Entry<?>> entries = metadata.getEntries();

        // Send the SpawnEntityPacket only to this player
        player.sendPacket(getEntityType().registry().spawnType().getSpawnPacket(this));
        player.sendPacket(new EntityMetaDataPacket(this.getEntityId(), entries));
    }

    // Despawn the NPC for a specific player
    public void despawnForPlayer(@NotNull Player player) {
        player.sendPacket(new DestroyEntitiesPacket(getEntityId()));
        this.remove();
    }
}
