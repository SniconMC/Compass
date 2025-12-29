package rip.snicon.compass.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityPose;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.monster.zombie.ZombieMeta;

import nub.wi1helm.template.npc.mob.TemplateMobNPC;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.PlayerRank;

public class ParkourNPC extends TemplateMobNPC {
    public ParkourNPC() {
        super(EntityType.ZOMBIE, MysteryInstanceType.HUB.getInstance(), new Pos(32.5, 10.0, 36.5, 165.0f, 0.0f));

        //setSkinLayer(SkinLayer.NO_CAPE);

        /*//setSkin(new PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTcyMzgxNDE4MTI3NywKICAicHJvZmlsZUlkIiA6ICI1MTAxNWY4ODQ1OTA0ZDA0ODg2MzUzZDhjOTYzZGQzMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBZGliMjM3MDQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFiMWYyZjdiZDU0YWQ0ZWFhNmE2Zjk1Zjk2MjQxZGU4MjQ2NjgwZjNhMTZmMDQzNzJjYmJjZTc1MThiZjE1NSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                "IuTUBN17kAZiAoTY9bdVURx+bYWGhLQF9ZRe4TcJMtNXSni+/djQPz4fvXSzvhZNtCRvquYuvK8z7KUoY8NNK3vtzkcj48HQ2PfqENaScPXExVQw0XOH30ufsNC0QheE0aVxhB8Po36QrMKftW7323I3a0PQ8QzSmkRX+k4cIqFFyYbaElqMKU8P/cuRAtsFK1lI53ZeK1KJUZMnTrtPpFfOw/huRbWa9V5gsPdKD0zGXHAa4Gnb/Umo56vJ00DkL7ZzxpDB3ZHD8ASgDsgyd3uSar/TcPOZb919LHpYOpG/ozZWgM7EiNNyI5pEbDo7urcMSupuWCy0eW5qIBp5bEzGkluiIjXtyJslSGlfpoh4fCFjbCSzRP8W0JhHeIL3W/0gnV26DE0dFM07sG1L+s0znNxmr0lnxcr2Oft4rvZcOZBui8mHPv3Kle8I6KJepzhB6ol2CDkoYNsPQz6ny2rZmYcXGX2pRFdifp/fkIKKojGKezVmfVg5KvTl286Tfqm3CgrWE4ZKy4utpl5MDWvPnt9Iq3UEp148oG3IRmCgL6hTElaYdvrCpjfBhU7vhQOeVEbV1DO4WNgkfgCHlP3x6VJdB+g6hh40fBVquJ8idRqkdoyXcxXeOfWe74VrfjFYJC8JVOFUBfmk4AfYfRF88WRt7PdCFWDBBxUsRoE="
        ));*/

        /*
        setName(new TemplateText(TextUtils.convertStringToComponent("<yellow>-1 Playing</yellow>")
        ,TextUtils.convertStringToComponent("<gold>Parkour <gray>[0.1]</gray></gold>")
        ,TextUtils.convertStringToComponent("<red>Reload = ☠</red>")));

         */
    }

    @Override
    public void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;

        editEntityMeta(ZombieMeta.class, zombieMeta -> {
            if (p.getDataHandler().getRank() == PlayerRank.EVOKER) {
                zombieMeta.setBaby(true);
                setPose(EntityPose.SITTING);
            } else {
                zombieMeta.setBaby(false);
                setPose(EntityPose.STANDING);
            }
        });
    }
}
