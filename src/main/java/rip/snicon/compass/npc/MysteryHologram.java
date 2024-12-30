package rip.snicon.compass.npc;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;
import java.util.UUID;

/**
 * Abstract base class for MysteryNPCs, handling initialization, spawning, visibility, and packet logic.
 */
public abstract class MysteryHologram extends Entity {

    private final int row;
    private double viewingDistance;
    private boolean exists = false;

    public MysteryHologram(int row, Pos position) {
        super(EntityType.TEXT_DISPLAY);
        this.row = row;
        this.position = position;

        setNoGravity(true);
        initialize(); // Call initialize once when NPC is created
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

    public void spawn(MysteryPlayer player) {
        exists = true; // Mark NPC as existing
        onSpawn(player);
        spawnHologramForPlayer(player);
    }

    public void despawn(MysteryPlayer player) {
        exists = false; // Mark NPC as not existing
        onDespawn(player);
        despawnHologramForPlayer(player);
    }

    // Packet Handling

    /**
     * Sends packets to spawn the NPC for a specific player.
     *
     * @param player The player who is seeing the NPC.
     */
    public void spawnHologramForPlayer(Player player) {

        player.sendPacket(getMetadataPacket());
        player.sendPacket(getEntityType().registry().spawnType().getSpawnPacket(this));

        super.updateNewViewer(player);
    }

    /**
     * Sends packets to remove the NPC from a player's view.
     *
     * @param player The player who is no longer seeing the NPC.
     */
    public void despawnHologramForPlayer(Player player) {
        player.sendPacket(new DestroyEntitiesPacket(this.getEntityId()));
        super.updateOldViewer(player);
    }

    // Getters and Setters


    public int getRow() {
        return row;
    }

    public void setText(Component text) {
        editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setText(text);
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
            meta.setPosRotInterpolationDuration(1);
        });
    }

    public void updateText(Component text, MysteryPlayer player) {
        editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setText(text);

        });
        player.sendPacket(this.getMetadataPacket());
    }


    public double getViewingDistance() {
        return viewingDistance;
    }

    public void setViewingDistance(double viewingDistance) {
        this.viewingDistance = viewingDistance;
    }
}

