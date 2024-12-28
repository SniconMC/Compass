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
import java.util.UUID;

/**
 * Abstract base class for MysteryNPCs, handling initialization, spawning, visibility, and packet logic.
 */
public abstract class MysteryHologram extends Entity {

    private String text;
    private double viewingDistance;
    private Pos defaultPos;
    private boolean exists = false;

    public MysteryHologram(EntityType type, String text, double viewingDistance, Pos defaultPos) {
        super(type);
        this.text = text;
        this.viewingDistance = viewingDistance;
        this.defaultPos = defaultPos;

        setNoGravity(true);
        initialize(); // Call initialize once when NPC is created
        registerEvents();
    }

    // Abstract Methods

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

        player.sendPacket(getMetadataPacket());
        player.sendPacket(getEntityType().registry().spawnType().getSpawnPacket(this));

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

    // Getters and Setters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text= text;
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

