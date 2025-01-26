package rip.snicon.compass.npc.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.metadata.PlayerMeta;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.debug.DebugContainer;
import rip.snicon.compass.npc.MysteryHologram;
import rip.snicon.compass.npc.MysteryNPC;
import rip.snicon.compass.npc.goals.LookAtPlayerGoal;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

// Example NPC Class
public class DebugJohnson extends MysteryNPC {

    public DebugJohnson() {
        super(EntityType.PLAYER, 1, 32, new Pos(26.5,9,20.5,0,0));
    }


    @Override
    public void initialize() {

        // Add AI group with LookAtPlayerGoal
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new LookAtPlayerGoal(this, 7, getDefaultPos())) // Look at players within 5 blocks
                        .build()
        );


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
                hologram.setText(TextUtils.convertStringToComponent("<gold>Debug Johnson</gold>"));
        }
    }

    @Override
    public void onDespawnHologram(MysteryHologram hologram, MysteryPlayer player) {

    }

    @Override
    public void onSpawn(MysteryPlayer player) {
        ;
        setInstance(MysteryInstanceType.HUB.getInstance(), getDefaultPos());

        setPlayerSkin(new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTczNzE0NDA0NjQ2NCwKICAicHJvZmlsZUlkIiA6ICIwMzBlMDA1OWQwY2M0YTZhODY3N2RkZWU3MjEzMjg1MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTbXVnRm9vZGllIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk5NGNmMDZiNTE4MmZhNTdlNDE2ZDFmZTIyOWY5MTE5YjI5N2Y5MDA4OGJhNDVlYzk0NWIzZmI4NWFkOWYzM2QiCiAgICB9CiAgfQp9", "mMjcrgCypBmaaAkix9vuAZcgKq7EU+eUaAlPxsSklu/vStL+D+ek1cEcJ4vbu/zaVxTEXPHAikBS5eyb+f3CHrvu2uCwyRh097IAzBjVMPSBAhEuijVzicL+rQK9SXU9kAPCuEzNZSEg8i73xXgzzQQMkw+/bGzoQXaO4yVWMStV7qTxCSTPv6WE0ob2rwy1EKsoJl1fUe/FME9Lijqyegotn89hHaZLn9diR5yzfdjo/fyLO0ipLLAYNIHx7cvCXN6cI87GQ1h/d9lhh586YJoW2fgididPoIbispioY+p6QzvgxnIqPKyIEcy5IbdcheRSCsFsTlAmtCcp4XBKxFbFhbBATP2BPk1tGPEWycYfmO9yc0qtArKDJWrq9SjRUhbIwI4/EC2Lza+QdDckxvV/dbiyF/7heXKwA2VmYMn4XgfCvgR4HtoQSZGXbby/I1LGQl9HYdtiY1uZZzmFu/v56RVknAIkjMc0x8zxIGAdQGL+hKwItsGGlw7qVoAP5Ky5jrjrU8/JrvLRvVdRPQN5H7YrZm8C2m2WKi2W6hxoYhzZuxyZGKVPoC69k4MK1WMzD5Y8RxRrgeCpUVPVeIwHQ3pD3+/d1TiJud64noLYbGgiH9Fnq9p5hMpFfMLFYay97kZlK5cLRSWe3X6iHsqQu8dwWZo8mZkMrNHhRMQ="));

    }

    @Override
    public void onDespawn(MysteryPlayer player) {

    }

    @Override
    public void onInteract(MysteryPlayer player) {
        player.openInventory(new DebugContainer().constructInventory(player));
    }
}
