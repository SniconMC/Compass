package rip.snicon.compass.npc;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

/**
 * Abstract base class for MysteryNPCs, handling initialization, spawning, visibility, and packet logic.
 */
public abstract class MysteryNPC extends EntityCreature {

    private String name;
    private PlayerSkin playerSkin;
    private double viewingDistance;
    private Pos defaultPos;
    private boolean exists = false;

    public MysteryNPC(EntityType type, String name, double viewingDistance, Pos defaultPos) {
        super(type);
        this.name = name;
        this.viewingDistance = viewingDistance;
        this.defaultPos = defaultPos;

        setNoGravity(true);
        initialize(); // Call initialize once when NPC is created
        registerEvents();
    }

    public MysteryNPC(EntityType type, String name, double viewingDistance, Pos defaultPos, PlayerSkin playerSkin) {
        this(type, name, viewingDistance, defaultPos);
        this.playerSkin = playerSkin;
    }

    // Abstract Methods

    /**
     * Static method to create and initialize all NPCs.
     */
    public static void create() {
        for (MysteryNPCType npcType : MysteryNPCType.values()) {
            MysteryNPC npc = npcType.getNpcInstance();
            npc.initialize(); // Ensure shared setup logic is called
        }
    }

    /**
     * Called once when the NPC is created. Shared setup logic goes here.
     */
    public abstract void initialize();

    /**
     * Triggered when the NPC spawns for a specific player.
     *
     * @param player The player for whom the NPC spawns.
     */
    public abstract void onSpawn(MysteryPlayer player);

    /**
     * Triggered when the NPC despawns for a specific player.
     *
     * @param player The player for whom the NPC despawns.
     */
    public abstract void onDespawn(MysteryPlayer player);

    /**
     * Triggered when a player interacts with the NPC.
     *
     * @param player The player interacting with the NPC.
     */
    public abstract void onInteract(MysteryPlayer player);

    // Centralized Spawn and Despawn Logic

    public void handleSpawnLogic(MysteryPlayer player) {
        Pos playerPosition = player.getPosition();
        boolean isWithinDistance = this.getDistance(playerPosition) <= viewingDistance;

        if (isWithinDistance && !exists) {
            spawn(player); // Only spawn if NPC doesn't already exist
        } else if (!isWithinDistance && exists) {
            despawn(player); // Only despawn if NPC exists
        }
    }

    private void spawn(MysteryPlayer player) {
        exists = true; // Mark NPC as existing
        onSpawn(player);
        updateNewViewer(player);
    }

    private void despawn(MysteryPlayer player) {
        exists = false; // Mark NPC as not existing
        onDespawn(player);
        updateOldViewer(player);
    }

    // Packet Handling

    /**
     * Sends packets to spawn the NPC for a specific player.
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
     * Sends packets to remove the NPC from a player's view.
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
     * Registers events related to the NPC, such as spawning, despawning, and interactions.
     */
    private void registerEvents() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            MysteryPlayer player = (MysteryPlayer) event.getPlayer();
            spawn(player); // Trigger NPC spawn for new players
        });

        globalEventHandler.addListener(PlayerMoveEvent.class, event -> {
            MysteryPlayer player = (MysteryPlayer) event.getPlayer();
            handleSpawnLogic(player);
        });

        globalEventHandler.addListener(PlayerEntityInteractEvent.class, event -> {
            MysteryPlayer player = (MysteryPlayer) event.getPlayer();
            if (event.getTarget() == this) {
                this.onInteract(player);
            }

        });
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

    public Pos getDefaultPos() {
        return defaultPos;
    }

    public void setDefaultPos(Pos defaultPos) {
        this.defaultPos = defaultPos;
    }
}
