package rip.snicon.modules.oblivion;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.goal.RangedAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
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
    private boolean shouldLookAtPlayers;
    private final float originalYaw;
    private final float originalPitch;

    public NPC(@NotNull String name, PlayerSkin playerSkin,
               @NotNull Instance instance, @NotNull Pos position, @NotNull Consumer<Player> onClick) {
        super(EntityType.PLAYER);
        this.name = name;
        this.skin = playerSkin;
        this.position = position;
        this.onClick = onClick;
        this.uuid = UUID.randomUUID();
        this.entityId = (int) (Math.random() * Integer.MAX_VALUE);
        this.shouldLookAtPlayers = true; // Default to true
        this.originalYaw = position.yaw(); // Store the original yaw
        this.originalPitch = position.pitch();

        this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);
        setInstance(instance, position);

        // Register event listeners
        MinecraftServer.getGlobalEventHandler().addListener(EntityAttackEvent.class, this::handle)
                .addListener(PlayerEntityInteractEvent.class, this::handle);

        // Add AI group with LookAtPlayerGoal
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new LookAtPlayerGoal(this, 5)) // Look at players within 5 blocks
                        .build()
        );
    }

    public void setShouldLookAtPlayers(boolean shouldLookAtPlayers) {
        this.shouldLookAtPlayers = shouldLookAtPlayers;
    }

    public boolean shouldLookAtPlayers() {
        return shouldLookAtPlayers;
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

    private static final class LookAtPlayerGoal extends GoalSelector {
        private Entity target;
        private final double range;  // Range within which the NPC should look at players
        private final float originalYaw;
        private final float originalPitch;

        public LookAtPlayerGoal(EntityCreature entityCreature, double range) {
            super(entityCreature);
            this.range = range;
            this.originalYaw = entityCreature.getPosition().yaw(); // Capture the original yaw
            this.originalPitch = entityCreature.getPosition().pitch();
        }

        @Override
        public boolean shouldStart() {
            if (!((NPC) entityCreature).shouldLookAtPlayers()) {
                return false;
            }
            target = findTarget();
            return target != null;
        }

        @Override
        public void start() {
            // No action needed on start
        }

        @Override
        public void tick(long time) {
            if (!((NPC) entityCreature).shouldLookAtPlayers()) {
                target = null;
                return;
            }

            if (target == null || entityCreature.getDistanceSquared(target) > range * range ||
                    entityCreature.getInstance() != target.getInstance()) {
                target = null;
                // Reset the entity's yaw to the original yaw by teleporting it to the same position with the updated yaw
                entityCreature.teleport(entityCreature.getPosition().withYaw(originalYaw).withPitch(originalPitch));
                return;
            }

            entityCreature.lookAt(target);
        }

        @Override
        public boolean shouldEnd() {
            return target == null || !((NPC) entityCreature).shouldLookAtPlayers();
        }

        @Override
        public void end() {
            // Reset to the original yaw when ending the goal by teleporting it to the same position with the updated yaw
            entityCreature.teleport(entityCreature.getPosition().withYaw(originalYaw).withPitch(originalPitch));
        }

        public Entity findTarget() {
            // Simple logic to find the closest player within the specified range
            return entityCreature.getInstance().getEntities()
                    .stream()
                    .filter(entity -> entity instanceof Player)
                    .min((e1, e2) -> Double.compare(entityCreature.getDistanceSquared(e1), entityCreature.getDistanceSquared(e2)))
                    .orElse(null);
        }
    }
}
