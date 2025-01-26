package rip.snicon.compass.npc;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.scoreboard.Team;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for MysteryNPCs, handling initialization, spawning, visibility, and packet logic.
 */
public abstract class MysteryNPC extends EntityCreature {

    private int rowAmount;
    private double viewingDistance;
    private Pos defaultPos;
    private boolean exists = false;
    private final List<MysteryHologram> holograms = new ArrayList<>();
    private String npcIdentifier;
    private PlayerSkin playerSkin;

    public MysteryNPC(EntityType type, int rowAmount, double viewingDistance, Pos defaultPos) {
        super(type);
        this.rowAmount = rowAmount;
        this.viewingDistance = viewingDistance;
        this.defaultPos = defaultPos;

        setNoGravity(true);
        initialize(); // Base initialization
        registerEvents();
        createTextEntity(); // Create holograms
    }

    // Static Methods

    /**
     * Static method to create and initialize all NPCs.
     */
    public static void create() {

        Team team = MinecraftServer.getTeamManager().createTeam("NPC");
        team.setNameTagVisibility(TeamsPacket.NameTagVisibility.NEVER);
        team.setCollisionRule(TeamsPacket.CollisionRule.NEVER);

        for (MysteryNPCType npcType : MysteryNPCType.values()) {
            MysteryNPC npc = npcType.getNpcInstance();
            npc.initialize(); // Ensure shared setup logic is called
            team.addMember(npc.getNpcIdentifier());
        }
    }


    // Abstract Methods for Subclass Customization
    public abstract void initialize();
    public abstract void onSpawnHologram(MysteryHologram hologram, MysteryPlayer player);
    public abstract void onDespawnHologram(MysteryHologram hologram, MysteryPlayer player);
    public abstract void onSpawn(MysteryPlayer player);
    public abstract void onDespawn(MysteryPlayer player);
    public abstract void onInteract(MysteryPlayer player);

    private void createTextEntity() {
        double offset = 0.3;
        double baseHeight = this.getEntityType().height();

        for (int i = 0; i < rowAmount; i++) {
            Pos hologramPos = defaultPos.add(0, baseHeight + (i+1) * offset, 0);
            MysteryHologram hologram = new MysteryHologram(i, hologramPos) {
                @Override
                public void initialize() {
                    // Subclass can customize via NPC's onSpawnHologram
                }

                @Override
                public void onSpawn(MysteryPlayer player) {
                    onSpawnHologram(this, player);
                }

                @Override
                public void onDespawn(MysteryPlayer player) {
                    onDespawnHologram(this, player);
                }

                @Override
                public void onInteract(MysteryPlayer player) {
                    // Define interaction logic here
                }
            };
            holograms.add(hologram);
        }
    }

    // Spawn/Despawn Logic
    private void spawn(MysteryPlayer player) {
        exists = true;
        onSpawn(player);
        spawnNPCForPlayer(player);
        holograms.forEach(hologram -> hologram.spawn(player));
    }

    private void despawn(MysteryPlayer player) {
        exists = false;
        onDespawn(player);
        despawnNPCForPlayer(player);
        holograms.forEach(hologram -> hologram.despawn(player));
    }

    // Event Registration
    private void registerEvents() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            MysteryPlayer player = (MysteryPlayer) event.getPlayer();
            spawn(player);
        });

        globalEventHandler.addListener(PlayerMoveEvent.class, event -> {
            MysteryPlayer player = (MysteryPlayer) event.getPlayer();
            handleSpawnLogic(player);
        });

        globalEventHandler.addListener(PlayerEntityInteractEvent.class, event -> {
            MysteryPlayer player = (MysteryPlayer) event.getPlayer();
            if (event.getTarget() == this && event.getHand() == Player.Hand.MAIN) {
                this.onInteract(player);
            }
        });
        globalEventHandler.addListener(EntityAttackEvent.class, event -> {
            if (event.getEntity().getEntityType() != EntityType.PLAYER) {
                return;
            }
            MysteryPlayer player = (MysteryPlayer) event.getEntity();
            if (event.getTarget() == this) {
                this.onInteract(player);
            }
        });

    }

    public void handleSpawnLogic(MysteryPlayer player) {
        Pos playerPosition = player.getPosition();
        boolean isWithinDistance = this.getDistance(playerPosition) <= viewingDistance;

        if (isWithinDistance && !exists) {
            spawn(player);
        } else if (!isWithinDistance && exists) {
            despawn(player);
        }
    }

    // Packet Handling
    public void spawnNPCForPlayer(Player player) {
        Entity mount = null;

        // Check if the NPC should be sitting
        if (getPose() == Pose.SITTING) {
            // Create a mount entity (e.g., a CHICKEN or ARMOR_STAND)
            mount = new Entity(EntityType.ARMOR_STAND);
            mount.editEntityMeta(ArmorStandMeta.class, meta -> {
                meta.setSmall(true);
                meta.setInvisible(true);
                meta.setHasNoGravity(true);
            });
            mount.setInstance(MysteryInstanceType.HUB.getInstance(), getDefaultPos().add(0,-0.5,0));

            player.sendPacket(new DestroyEntitiesPacket(mount.getEntityId())); // Remove previous instance if any
            mount.addPassenger(this);
            player.sendPacket(mount.getEntityType().registry().spawnType().getSpawnPacket(mount));

            // Spawn the mount for the player
            mount.updateNewViewer(player);
        }

        // Regular NPC spawning logic
        if (getEntityType() == EntityType.PLAYER) {
            List<PlayerInfoUpdatePacket.Property> properties = (playerSkin != null)
                    ? List.of(new PlayerInfoUpdatePacket.Property("textures", playerSkin.textures(), playerSkin.signature()))
                    : List.of();

            PlayerInfoUpdatePacket.Entry entry = new PlayerInfoUpdatePacket.Entry(
                    this.getUuid(), npcIdentifier, properties, false, 0, GameMode.SURVIVAL, null, null
            );

            player.sendPacket(new PlayerInfoRemovePacket(this.getUuid())); // Ensure no duplicates
            player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));
        }

        super.updateNewViewer(player);

        // Send mount packet if the NPC is sitting
        if (mount != null) {
            mount.updateNewViewer(player);
        }
    }


    public void despawnNPCForPlayer(Player player) {
        if (getEntityType() == EntityType.PLAYER) {
            player.sendPacket(new PlayerInfoRemovePacket(this.getUuid()));
        } else {
            player.sendPacket(new DestroyEntitiesPacket(this.getEntityId()));
        }

        super.updateOldViewer(player);
    }

    // Getters and Setters
    public int getRowAmount() {
        return rowAmount;
    }

    public void setRowAmount(int rowAmount) {
        this.rowAmount = rowAmount;
    }

    public double getViewingDistance() {
        return viewingDistance;
    }

    public void setViewingDistance(double viewingDistance) {
        this.viewingDistance = viewingDistance;
    }

    public Pos getDefaultPos() {
        return defaultPos;
    }

    public void setDefaultPos(Pos defaultPos) {
        this.defaultPos = defaultPos;
    }

    public String getNpcIdentifier() {
        return npcIdentifier;
    }

    public void setNpcIdentifier(String npcIdentifier) {
        this.npcIdentifier = ("NPC " + npcIdentifier).substring(0, Math.min(16, ("NPC " + npcIdentifier).length()));
    }

    public PlayerSkin getPlayerSkin() {
        return playerSkin;
    }

    public void setPlayerSkin(PlayerSkin playerSkin) {
        this.playerSkin = playerSkin;
    }
}
