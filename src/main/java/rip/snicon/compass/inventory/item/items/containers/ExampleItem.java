package rip.snicon.compass.inventory.item.items.containers;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.monster.zombie.ZombieMeta;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;
import java.util.UUID;

public class ExampleItem extends MysteryItem {

    public ExampleItem() {
        super(Material.DANDELION, "Example Dandelion", List.of(

        ), 1, MysteryItemOrigin.PLAYER);

        setShowTooltip(true);
        setDyeColor("");
        setGlint(true);
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        // Add any dynamic player-specific properties here.
    }

    @Override
    public void onUse(MysteryPlayer player) {
        Entity entity = new LivingEntity(EntityType.ZOMBIE, UUID.randomUUID());
        entity.editEntityMeta(ZombieMeta.class, zombieMeta -> {
            zombieMeta.setCustomName(Component.text("Example Johnson"));
            zombieMeta.setCustomNameVisible(true);
            zombieMeta.setOnFire(true);
        });


        player.sendPacket(entity.getEntityType().registry().spawnType().getSpawnPacket(entity));
        player.sendPacket(entity.getMetadataPacket());
        player.sendPacket(new EntityTeleportPacket(entity.getEntityId(), player.getPosition(), false));
    }
}