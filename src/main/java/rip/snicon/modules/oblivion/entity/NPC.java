package rip.snicon.modules.oblivion.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import rip.snicon.modules.oblivion.OblivionConfig;
import rip.snicon.modules.oblivion.json.Oblivion;
import rip.snicon.modules.oblivion.json.OblivionSkin;

import java.util.*;
import java.util.function.Consumer;

public final class NPC extends EntityCreature {
    private final String name;
    private final PlayerSkin skin;
    private final Pos position;
    private final Consumer<Player> onClick;
    private final OblivionConfig config;
    private boolean shouldLookAtPlayers;
    private final float originalYaw;
    private final float originalPitch;

    public NPC(@NotNull String name, PlayerSkin playerSkin,
               @NotNull Instance instance, @NotNull Pos position, @NotNull Consumer<Player> onClick, OblivionConfig oblivionConfig, EntityType entityType) {
        super(entityType);
        this.name = name;
        this.skin = playerSkin;
        this.position = position;
        this.onClick = onClick;
        this.config = oblivionConfig;
        this.shouldLookAtPlayers = true; // Default to true
        this.originalYaw = position.yaw(); // Store the original yaw
        this.originalPitch = position.pitch();


        this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);
        this.getAttribute(Attribute.GENERIC_STEP_HEIGHT).setBaseValue(1.0);

        setInstance(instance, position);

        Team hiddenName = MinecraftServer.getTeamManager().getTeam("hidden_name");

        setTeam(hiddenName);
        hiddenName.addMember(name);

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

        if (this.entityType == EntityType.PLAYER){
            var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();
            if (skin.textures() != null && skin.signature() != null) {
                properties.add(new PlayerInfoUpdatePacket.Property("textures", skin.textures(), skin.signature()));
            }

            var entry = new PlayerInfoUpdatePacket.Entry(this.getUuid(), name, properties, false,
                    0, GameMode.SURVIVAL, null, null);
            player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));

            // Send the SpawnEntityPacket only to this player
            player.sendPacket(new SpawnEntityPacket(
                    getEntityId(),
                    this.getUuid(),
                    this.getEntityType().id(),
                    position,
                    position.yaw(),
                    0,
                    (short) 0,  // velocity x
                    (short) 0,  // velocity y
                    (short) 0   // velocity z
            ));

            // Send EntityMetadataPacket to apply metadata only to this player
            player.sendPacket(setSkinParts(this.config));
        } else {
            // Send the SpawnEntityPacket only to this player
            player.sendPacket(new SpawnEntityPacket(
                    getEntityId(),
                    this.getUuid(),
                    this.getEntityType().id(),
                    position,
                    position.yaw(),
                    0,
                    (short) 0,  // velocity x
                    (short) 0,  // velocity y
                    (short) 0   // velocity z
            ));
        }
    }

    public EntityMetaDataPacket setSkinParts(OblivionConfig config) {
        byte skinPartsBitmask = buildSkinPartsBitmask(config.getSkin());

        return new EntityMetaDataPacket(this.getEntityId(), Map.of(
                17, Metadata.Byte(skinPartsBitmask)));
    }

    public byte buildSkinPartsBitmask(OblivionSkin skin) {
        byte bitmask = 0;

        if (skin.isCape()) bitmask |= 0x01;
        if (skin.isJacket()) bitmask |= 0x02;
        if (skin.isLeft_sleeve()) bitmask |= 0x04;
        if (skin.isRight_sleeve()) bitmask |= 0x08;
        if (skin.isLeft_pants()) bitmask |= 0x10;
        if (skin.isRight_pants()) bitmask |= 0x20;
        if (skin.isHat()) bitmask |= 0x40;

        return bitmask;
    }

    // Despawn the NPC for a specific player
    public void despawnForPlayer(@NotNull Player player) {
        if (this.entityType == EntityType.PLAYER){
            player.sendPacket(new PlayerInfoRemovePacket(this.getUuid()));
        }

        player.sendPacket(new DestroyEntitiesPacket(getEntityId()));
        this.remove();
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
