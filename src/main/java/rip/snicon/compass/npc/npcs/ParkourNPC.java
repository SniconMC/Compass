package rip.snicon.compass.npc.npcs;

import io.grpc.Server;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.PlayerMeta;
import rip.snicon.compass.Main;
import rip.snicon.compass.ServerRegistry;
import rip.snicon.compass.database.redisdb.RedisCacheManager;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.npc.MysteryHologram;
import rip.snicon.compass.npc.MysteryNPC;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;
import java.util.Map;

public class ParkourNPC extends MysteryNPC {

    public ParkourNPC() {
        super(
                EntityType.PLAYER,
                3,
                32, // Viewing distance (adjust as needed)
                new Pos(32.5, 10.0, 36.5, 165.0f, 0.0f) // Position from JSON
        );
    }

    @Override
    public void initialize() {
        // Set skin attributes from JSON
        setPlayerSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTcyMzgxNDE4MTI3NywKICAicHJvZmlsZUlkIiA6ICI1MTAxNWY4ODQ1OTA0ZDA0ODg2MzUzZDhjOTYzZGQzMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBZGliMjM3MDQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFiMWYyZjdiZDU0YWQ0ZWFhNmE2Zjk1Zjk2MjQxZGU4MjQ2NjgwZjNhMTZmMDQzNzJjYmJjZTc1MThiZjE1NSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                "IuTUBN17kAZiAoTY9bdVURx+bYWGhLQF9ZRe4TcJMtNXSni+/djQPz4fvXSzvhZNtCRvquYuvK8z7KUoY8NNK3vtzkcj48HQ2PfqENaScPXExVQw0XOH30ufsNC0QheE0aVxhB8Po36QrMKftW7323I3a0PQ8QzSmkRX+k4cIqFFyYbaElqMKU8P/cuRAtsFK1lI53ZeK1KJUZMnTrtPpFfOw/huRbWa9V5gsPdKD0zGXHAa4Gnb/Umo56vJ00DkL7ZzxpDB3ZHD8ASgDsgyd3uSar/TcPOZb919LHpYOpG/ozZWgM7EiNNyI5pEbDo7urcMSupuWCy0eW5qIBp5bEzGkluiIjXtyJslSGlfpoh4fCFjbCSzRP8W0JhHeIL3W/0gnV26DE0dFM07sG1L+s0znNxmr0lnxcr2Oft4rvZcOZBui8mHPv3Kle8I6KJepzhB6ol2CDkoYNsPQz6ny2rZmYcXGX2pRFdifp/fkIKKojGKezVmfVg5KvTl286Tfqm3CgrWE4ZKy4utpl5MDWvPnt9Iq3UEp148oG3IRmCgL6hTElaYdvrCpjfBhU7vhQOeVEbV1DO4WNgkfgCHlP3x6VJdB+g6hh40fBVquJ8idRqkdoyXcxXeOfWe74VrfjFYJC8JVOFUBfmk4AfYfRF88WRt7PdCFWDBBxUsRoE="
        ));

        // Set player meta attributes from JSON
        editEntityMeta(PlayerMeta.class, meta -> {
            meta.setCapeEnabled(false);
            meta.setJacketEnabled(true);
            meta.setLeftSleeveEnabled(true);
            meta.setRightSleeveEnabled(true);
            meta.setLeftLegEnabled(true);
            meta.setRightLegEnabled(true);
            meta.setHatEnabled(true);
        });
    }

    @Override
    public void onSpawnHologram(MysteryHologram hologram, MysteryPlayer player) {
        switch (hologram.getRow()) {
            case 0:
                hologram.setText(TextUtils.convertStringToComponent("<yellow>-1 Playing</yellow>"));
                break;
            case 1:
                hologram.setText(TextUtils.convertStringToComponent("<gold>Parkour <gray>[0.1]</gray></gold>"));
                break;
            case 2:
                hologram.setText(TextUtils.convertStringToComponent("<red>Reload = ☠</red>"));
                break;
        }
    }

    public void onDespawnHologram(MysteryHologram hologram, MysteryPlayer player) {

    }

    @Override
    public void onSpawn(MysteryPlayer player) {
        setInstance(MysteryInstanceType.HUB.getInstance(), getDefaultPos());
    }

    @Override
    public void onDespawn(MysteryPlayer player) {
        // Handle despawning logic if necessary
    }

    @Override
    public void onInteract(MysteryPlayer player) {
        ServerRegistry.connectPlayerToProxyWithLabel(player, "minigame");
    }
}
