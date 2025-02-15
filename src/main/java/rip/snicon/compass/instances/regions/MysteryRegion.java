package rip.snicon.compass.instances.regions;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.particle.Particle;
import net.minestom.server.tag.Tag;
import rip.snicon.compass.Main;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

import java.util.*;

public enum MysteryRegion {
    SPAWN("<white>Spawn</white>", List.of(new MysteryChunk(new Pos(16, 100,10), new Pos(70, -20,70) ), new MysteryChunk(new Pos(16, 100,52), new Pos(-14, -20,-15) ), new MysteryChunk(new Pos(6, 100,-15), new Pos(-14, -20,-30) ), new MysteryChunk(new Pos(1, -20, 52), new Pos(16, 100, 700))), 0.5, 1),
    TOWN("<yellow>Town</yellow>", List.of(new MysteryChunk(new Pos(16, 100,10), new Pos(74, -20,-57)), new MysteryChunk(new Pos(16, 100,-15), new Pos(6, -20,-53) )),0.5, 1),
    PARKOUR_CENTER("<aqua>Parkour Center</aqua>", List.of(new MysteryChunk(new Pos(70, 60,10), new Pos(160, -20,58) )), 0.5, 1),
    BLOCKHUNT_MANSION("<red>Blockhunt Masion</red>", List.of(new MysteryChunk(new Pos(1, -20, 52), new Pos(-25, 100, 132)), new MysteryChunk(new Pos(1, -20, 70), new Pos(50, 100, 92))), 0.5, 1);
    private final String displayName;
    private final List<MysteryChunk> chunks;
    private final double emeralds;
    private final double xp;

    private final Map<MysteryPlayer, Map<String, Entity>> playerRegionTexts = new HashMap<>();


    MysteryRegion(String displayName, List<MysteryChunk> chunks, double emeralds, double xp) {
        this.displayName = displayName;
        this.chunks = chunks;
        this.emeralds = emeralds;
        this.xp = xp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<MysteryChunk> getChunks() {
        return chunks;
    }

    public double getEmeralds() {
        return emeralds;
    }

    public double getXp() {
        return xp;
    }

    public boolean isInside(Pos position) {
        return chunks.stream().anyMatch(chunk -> chunk.isInside(position));
    }

    public List<MysteryRegion> getBorderingRegions() {
        List<MysteryRegion> borderingRegions = new ArrayList<>();

        for (MysteryRegion otherRegion : MysteryRegion.values()) {
            if (otherRegion == this) {
                continue; // Skip the current region
            }

            for (MysteryChunk thisChunk : this.getChunks()) {
                for (MysteryChunk otherChunk : otherRegion.getChunks()) {
                    if (thisChunk.isBordering(otherChunk)) {
                        borderingRegions.add(otherRegion);
                        break; // No need to check further chunks for this region
                    }
                }
            }
        }

        return borderingRegions;
    }

    public Pos findClosestPoint(Pos playerPosition) {
        Pos closestPoint = null;
        double minDistanceSquared = Double.MAX_VALUE;

        for (MysteryChunk chunk : chunks) {
            // Clamp player's position to the bounds of the chunk
            double clampedX = Math.max(chunk.getMin().x(), Math.min(chunk.getMax().x(), playerPosition.x()));
            double clampedY = Math.max(chunk.getMin().y(), Math.min(chunk.getMax().y(), playerPosition.y()));
            double clampedZ = Math.max(chunk.getMin().z(), Math.min(chunk.getMax().z(), playerPosition.z()));

            // Calculate squared distance to avoid expensive sqrt operation
            double distanceSquared = Math.pow(playerPosition.x() - clampedX, 2)
                    + Math.pow(playerPosition.y() - clampedY, 2)
                    + Math.pow(playerPosition.z() - clampedZ, 2);

            // Update the closest point if this one is closer
            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                closestPoint = new Pos(clampedX, clampedY, clampedZ);
            }
        }

        return closestPoint;
    }

    public void updateRegionText(MysteryPlayer player) {
        Pos closestPoint = this.findClosestPoint(player.getPosition());
        if (closestPoint != null) {
            double distanceSquared = Math.pow(player.getPosition().x() - closestPoint.x(), 2)
                    + Math.pow(player.getPosition().y() - closestPoint.y(), 2)
                    + Math.pow(player.getPosition().z() - closestPoint.z(), 2);

            if (distanceSquared <= 100 && distanceSquared > 1) { // Within range
                Entity text = getOrCreateRegionText(player, this.getDisplayName());
                teleportRegionText(player, text, closestPoint);
            } else {
                removeRegionText(player, this.getDisplayName());
            }
        }
    }

    private Entity getOrCreateRegionText(MysteryPlayer player, String regionName) {
        playerRegionTexts.putIfAbsent(player, new HashMap<>());
        Map<String, Entity> regionTexts = playerRegionTexts.get(player);

        return regionTexts.computeIfAbsent(regionName, rn -> {
            Entity text = new Entity(EntityType.TEXT_DISPLAY);
            text.setTag(Tag.String("region_text"), rn);
            text.editEntityMeta(TextDisplayMeta.class, meta -> {
                meta.setHasNoGravity(true);
                meta.setText(TextUtils.convertStringToComponent("<green>New region:</green> " + regionName));
                meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
                meta.setPosRotInterpolationDuration(2);
            });
            text.updateNewViewer(player);
            player.sendPacket(text.getMetadataPacket());
            return text;
        });
    }

    private void teleportRegionText(MysteryPlayer player, Entity text, Pos newPosition) {
        player.sendPacket(new EntityTeleportPacket(text.getEntityId(), newPosition.add(0,3,0), false));
    }

    private void removeRegionText(MysteryPlayer player, String regionName) {
        Map<String, Entity> regionTexts = playerRegionTexts.get(player);
        if (regionTexts != null) {
            Entity text = regionTexts.remove(regionName);
            if (text != null) {
                text.remove(); // Assuming a `remove` method exists for your entity class
                player.sendPacket(new DestroyEntitiesPacket(text.getEntityId())); // Inform the client
            }
        }
    }



    public void debug(MysteryPlayer player) {
        // Debug the current region's chunks
        chunks.forEach(chunk -> chunk.displayToPlayer(player, Particle.CRIT));
    }
}
