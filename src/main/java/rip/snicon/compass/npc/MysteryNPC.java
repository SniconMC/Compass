package rip.snicon.compass.npc;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.entity.metadata.PlayerMeta;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.utils.time.TimeUnit;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

/**
 * Abstract base class for MysteryNPCs, handling spawning, visibility, and interaction logic.
 */
public abstract class MysteryNPC extends EntityCreature {

    private String name;
    private PlayerSkin playerSkin;
    private double viewingDistance;
    private boolean exists = false;

    public MysteryNPC(EntityType type, String name, double viewingDistance) {
        super(type);

        setNoGravity(true);

        this.name = name;
        this.viewingDistance = viewingDistance;
        registerEvents();
    }

    public MysteryNPC(EntityType type, String name, double viewingDistance, PlayerSkin playerSkin) {
        this(type, name, viewingDistance);
        this.playerSkin = playerSkin;
    }

    // Abstract Methods

    /**
     * Determines if the NPC should spawn.
     *
     * @return true if the NPC should spawn, false otherwise.
     */
    protected boolean spawnCondition() {
        return true;
    }

    /**
     * Initializes the NPC.
     *
     * @param player The player for whom the NPC is initialized.
     */
    public abstract void initialize(MysteryPlayer player);

    /**
     * Triggered when the NPC spawns.
     */
    public abstract void onSpawn();

    /**
     * Triggered when the NPC disappears.
     */
    public abstract void onDisappear();

    /**
     * Triggered when a player interacts with the NPC.
     *
     * @param player The player interacting with the NPC.
     */
    public abstract void onInteract(MysteryPlayer player);

    // Static Methods

    /**
     * Creates and registers all NPCs on server startup.
     */
    public static void create() {
        for (MysteryNPCType npcType : MysteryNPCType.values()) {
            MysteryNPC npc = npcType.getNpcInstance();

        }
    }

    // Packet Handling

    /**
     * Sends packets to spawn the NPC for a new viewer.
     *
     * @param player The player who is seeing the NPC.
     */
    @Override
    public void updateNewViewer(Player player) {
        if (entityType == EntityType.PLAYER) {

            List<PlayerInfoUpdatePacket.Property> properties = (playerSkin != null)
                    ? List.of(new PlayerInfoUpdatePacket.Property("textures", playerSkin.textures(), playerSkin.signature()))
                    : List.of();

            PlayerInfoUpdatePacket.Entry entry = new PlayerInfoUpdatePacket.Entry(
                    this.getUuid(), name, properties, false, 0, GameMode.SURVIVAL, null, null
            );

            player.sendPacket(new PlayerInfoRemovePacket(this.getUuid()));
            player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));
        } else {
            player.sendPacket(getMetadataPacket());
            player.sendPacket(getEntityType().registry().spawnType().getSpawnPacket(this));
        }
        super.updateNewViewer(player);
    }

    /**
     * Sends packets to remove the NPC from an old viewer.
     *
     * @param player The player who is no longer seeing the NPC.
     */
    @Override
    public void updateOldViewer(Player player) {
        player.sendPacket(new PlayerInfoRemovePacket(this.getUuid()));
        super.updateOldViewer(player);
    }

    // Event Registration

    /**
     * Registers events related to the NPC, such as movement and interaction.
     */
    private void registerEvents() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            MysteryPlayer player = (MysteryPlayer) event.getPlayer();
            if (spawnCondition()) {
                initialize(player);
                updateNewViewer(player);
                onSpawn();
                exists = true;
            }
        });

        globalEventHandler.addListener(PlayerMoveEvent.class, this::handlePlayerMove);

        globalEventHandler.addListener(PlayerEntityInteractEvent.class, event -> {
            this.onInteract((MysteryPlayer) event.getPlayer());
        });
    }

    // Event Handlers

    private void handlePlayerMove(PlayerMoveEvent event) {
        MysteryPlayer player = (MysteryPlayer) event.getPlayer();
        Pos playerPosition = player.getPosition();

        if (exists) {
            if (this.getDistance(playerPosition) >= viewingDistance) {
                updateOldViewer(player);
                onDisappear();
                exists = false;
            }
        } else {
            if (this.getDistance(playerPosition) <= viewingDistance && spawnCondition()) {
                updateNewViewer(player);
                onSpawn();
                exists = true;
            }
        }
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerSkin getPlayerSkin() {
        return playerSkin;
    }

    public void setPlayerSkin(PlayerSkin playerSkin) {
        this.playerSkin = playerSkin;
    }

    public double getViewingDistance() {
        return viewingDistance;
    }

    public void setViewingDistance(double viewingDistance) {
        this.viewingDistance = viewingDistance;
    }

    public void setPosition(Pos position) {
        this.position = position;
    }
}
