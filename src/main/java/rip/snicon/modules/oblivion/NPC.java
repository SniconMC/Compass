package rip.snicon.modules.oblivion;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class NPC extends EntityCreature {
    private final String name;
    private final PlayerSkin skin;
    private final Pos position;
    private final Consumer<Player> onClick;
    private final UUID uuid;
    private final int entityId;

    public NPC(@NotNull String name, PlayerSkin playerSkin,
               @NotNull Instance instance, @NotNull Pos position, @NotNull Consumer<Player> onClick) {
        super(EntityType.PLAYER);  // Use the EntityType.PLAYER to make the server recognize this as a player-like entity
        this.name = name;
        this.skin = playerSkin;
        this.position = position;
        this.onClick = onClick;
        this.uuid = UUID.randomUUID();  // Unique ID for the NPC
        this.entityId = (int) (Math.random() * Integer.MAX_VALUE);

        setNoGravity(true);

        // Set instance and initial position
        setInstance(instance, position);

        // Register event listeners
        MinecraftServer.getGlobalEventHandler().addListener(EntityAttackEvent.class, this::handle)
                .addListener(PlayerEntityInteractEvent.class, this::handle);
    }

    // Server-side events now trigger as the entity is managed by the server
    public void handle(@NotNull EntityAttackEvent event) {
        if (event.getTarget() != this) return;
        if (!(event.getEntity() instanceof Player player)) return;
        onClick.accept(player);
    }

    public void handle(@NotNull PlayerEntityInteractEvent event) {
        if (event.getTarget() != this) return;
        if (event.getHand() != Player.Hand.MAIN) return;  // Prevent duplicating event
        onClick.accept(event.getEntity());
    }

    // Send packets to make the NPC visible only to a specific player
    public void makeVisibleTo(@NotNull Player player) {
        var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();
        if (skin.textures() != null && skin.signature() != null) {
            properties.add(new PlayerInfoUpdatePacket.Property("textures", skin.textures(), skin.signature()));
        }

        var entry = new PlayerInfoUpdatePacket.Entry(uuid, name, properties, false,
                0, GameMode.SURVIVAL, null, null);
        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));

        // Send the SpawnEntityPacket only to this player
        player.sendPacket(new SpawnEntityPacket(
                getEntityId(),
                uuid,
                EntityType.PLAYER.id(),
                position,
                position.yaw(),
                0,
                (short) 0,  // velocity x
                (short) 0,  // velocity y
                (short) 0   // velocity z
        ));

        // Send EntityMetadataPacket to apply metadata only to this player
        player.sendPacket(new EntityMetaDataPacket(getEntityId(), Map.of(17, Metadata.Byte((byte) 127))));
    }

    // Despawn the NPC for a specific player
    public void despawnForPlayer(@NotNull Player player) {
        player.sendPacket(new PlayerInfoRemovePacket(uuid));
        player.sendPacket(new DestroyEntitiesPacket(getEntityId()));
    }
}
